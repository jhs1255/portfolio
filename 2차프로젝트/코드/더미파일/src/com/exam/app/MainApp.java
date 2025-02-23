package com.exam.app;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			URL fxmlPath = new File("src/com/exam/app/view/MovieSetting.fxml").toURL();
			FXMLLoader loader = new FXMLLoader();
			AnchorPane mainLayoutAnchorPane = (AnchorPane) loader.load(fxmlPath);
			Scene scene = new Scene(mainLayoutAnchorPane);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("관리자 화면");
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
