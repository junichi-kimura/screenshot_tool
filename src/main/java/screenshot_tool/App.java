package screenshot_tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
			try {
				log(e);
			} catch (IOException e1) {
			}
		});
		Screenshot screenshot = new Screenshot();
		
		LogManager.getLogManager().reset();
		
		BorderPane border = new BorderPane();
		HBox hbox = new HBox();
		
		TextArea textArea = new TextArea();
        Button startButton = new Button("launch chrome");
        Button screenshotButton = new Button("take screenshot");
        screenshotButton.setDisable(true);
        screenshotButton.setOnAction((ActionEvent) -> {
        	File f = screenshot.getScreenshot(Screenshot.driver.getCurrentUrl(), textArea.getText());
        	String path = System.getProperty("user.home") + File.separator + ".screenshot_tool";
    		File dir = new File(path);
    		if (dir.exists() || dir.mkdir()) {
    			try {
					FileUtils.copyFile(f, new File(path + File.separator + System.currentTimeMillis() + ".png"));
    			} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
    		}
        });
        
        Button shutdownButton = new Button("shutdown");
        shutdownButton.setDisable(true);
        startButton.setOnAction((ActionEvent)-> {
        	startButton.setDisable(true);
        	screenshotButton.setDisable(false);
        	shutdownButton.setDisable(false);
        	screenshot.start();
        });
        
        shutdownButton.setOnAction((ActionEvent) -> {
        	startButton.setDisable(false);
        	screenshotButton.setDisable(true);
        	shutdownButton.setDisable(true);
        	screenshot.quit();
        });
        
        hbox.getChildren().addAll(startButton, screenshotButton, shutdownButton);
        border.setTop(hbox);
        VBox vbox = new VBox(textArea);
        border.setCenter(vbox);
        
        Scene scene1 = new Scene(new StackPane(border), 300, 100);
        
        stage.setTitle("screen shot tool");
        stage.setScene(scene1);
        stage.setOnCloseRequest(event -> {
        	
        });
        stage.show();
	}
	
	public void log(Throwable e) throws IOException {
		String path = System.getProperty("user.home") + File.separator + ".screenshot_tool" + File.separator + "log";
		File dir = new File(path);
		if (dir.exists() || dir.mkdir()) {
			File logFile = new File(path + File.separator + System.currentTimeMillis() + ".log");
			logFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(logFile);
			fos.write(ExceptionUtils.getStackTrace(e).getBytes());
			fos.close();
		}
	}

	public static void main(String...args) throws IOException {
		launch();
	}
}
