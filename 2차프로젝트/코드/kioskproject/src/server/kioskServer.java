package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class kioskServer extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("/fxml/server.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("키오스크 서버");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
