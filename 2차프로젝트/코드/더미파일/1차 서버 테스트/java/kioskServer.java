package kioskdemo;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class kioskServer extends Application {

	ServerSocket serversocket;
	public static ExecutorService threadPool;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("server.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("키오스크 서버");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
