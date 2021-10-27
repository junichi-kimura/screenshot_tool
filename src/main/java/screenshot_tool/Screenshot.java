package screenshot_tool;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.collect.ImmutableMap;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Screenshot {

	public static WebDriverRule driverRule = new WebDriverRule();
	public static ChromeDriver driver;
	
	public Screenshot() {
		WebDriverManager.chromedriver().setup();
	}
	
	public void start() {
		driver = driverRule.getPCWebDriver();
		driver.get(null);
	}
	
	public File getScreenshot(String url, String script) {
		File file = null;
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			if (!script.isEmpty()) {
				executor.executeScript(script);
			}
			file = getFullPageScreenshotAs(OutputType.FILE);
		} catch (Exception e) {
			System.out.println(e);
		}
		return file;
	}
	
	@SuppressWarnings("unchecked")
	public <X> X getFullPageScreenshotAs(OutputType<X> outputType) {

		// get current layout
		Map<String, Object> layoutMetrics = driver.executeCdpCommand("Page.getLayoutMetrics", Collections.emptyMap());

		// override device metrics
		Map<String, Long> contentSize = (Map<String, Long>) layoutMetrics.get("contentSize");
		long width = contentSize.get("width");
		long height = contentSize.get("height");
		driver.executeCdpCommand("Emulation.setDeviceMetricsOverride",
				ImmutableMap.of("mobile", true, "width", width, "height", height, "deviceScaleFactor", 1));

		// capture screenshot
		Map<String, Object> clip = ImmutableMap.of("x", 0, "y", 0, "width", width, "height", height, "scale", 1);
		Map<String, Object> result = driver.executeCdpCommand("Page.captureScreenshot", ImmutableMap.of("clip", clip));

		// restore device metrics
		Map<String, Long> visualViewport = (Map<String, Long>) layoutMetrics.get("layoutViewport");
		long clientWidth = visualViewport.get("clientWidth");
		long clientHeight = visualViewport.get("clientHeight");
		driver.executeCdpCommand("Emulation.setDeviceMetricsOverride",
				ImmutableMap.of("mobile", true, "width", clientWidth, "height", clientHeight, "deviceScaleFactor", 1));

		String base64 = (String) result.get("data");

		return outputType.convertFromBase64Png(base64);
	}
	
	public void quit() {
		driver.quit();
	}
}
