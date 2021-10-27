package screenshot_tool;

import java.util.Collections;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class WebDriverRule {
	public static ChromeDriver driver;
	public static ChromeDriver getDriver() {
		return driver;
	}
	public ChromeOptions chromeOptions;

	// ウィンドウサイズ設定
	public static int windowWidth = 1520;
	public static int windowHeight = 950;
	public static int windowWidthSp = 375;
	public static int windowHeightSp = 812;
	
	// 待機時間設定
	public int waitSec = 5;
	public int waitSecShort = 1;
	
	/**
	 * PC用WebDriver設定
	 * @return
	 */
	public ChromeDriver getPCWebDriver() {
		chromeOptions = this.getChromeOptions();
		driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	/**
	 * SP用WebDriver設定
	 * @return
	 */
	public ChromeDriver getSPWebDriver() {
		chromeOptions = this.getSpChromeOptions();
		driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	
	/**
	 * ChromeOptions設定
	 * @return ChromeOptions
	 */
	public ChromeOptions getChromeOptions() {
		ChromeOptions chromeOptions = new ChromeOptions();
		// chromedriverディレクトリ設定
		chromeOptions = getBaseChromeOptions(chromeOptions);
		// ウィンドウサイズ指定
		chromeOptions.addArguments("window-size=" + windowWidth + "," + windowHeight);
		return chromeOptions;
	}

	/**
	 * SP用ChromeOptions設定
	 * @return ChromeOptions
	 */
	public ChromeOptions getSpChromeOptions() {
		ChromeOptions spChromeOptions = new ChromeOptions();
		
		// chromedriverディレクトリ設定
		spChromeOptions = getBaseChromeOptions(spChromeOptions);
		// ウィンドウサイズ指定
		spChromeOptions.addArguments("window-size=" + windowWidthSp + "," + windowHeightSp);
		// UAをSPに設定
		spChromeOptions.addArguments("--user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/69.0.3497.91 Mobile/15E148 Safari/605.1");
		return spChromeOptions;
	}
	
	/**
	 * ChromeOptions 共通設定
	 * @param chromeOptions
	 * @return
	 */
	public ChromeOptions getBaseChromeOptions(ChromeOptions chromeOptions) {
		// シークレットウィンドウで開く
		chromeOptions.addArguments("--incognito");
		// sandbox無効化
		chromeOptions.addArguments("--no-sandbox");
		// シェアドメモリファイルの保持場所を/dev/shmの代わりに/tmpディレクトリを使用する設定
		chromeOptions.addArguments("--disable-dev-shm-usage");
		// SSL証明書警告画面を回避
		chromeOptions.addArguments("--ignore-certificate-errors");
		// 「自動テストソフトウェアによって制御されています」を非表示
		chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		return chromeOptions;
	}
}
