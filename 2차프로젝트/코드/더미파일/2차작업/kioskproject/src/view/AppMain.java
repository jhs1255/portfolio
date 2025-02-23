package view;

import controller.Kioskcontroller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {
	public static Kioskcontroller kioskcontroller = null;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("/fxml/kioskMain.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("kiosk");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		kioskcontroller = new Kioskcontroller("127.0.0.1", 50001, root);
		kioskcontroller.connectServer();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
