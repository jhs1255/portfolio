package Demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	public static Kioskcontroller kioskcontroller = null;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		Scene scene = new Scene(root,700,800);
		
		primaryStage.setTitle("데모파일");
		primaryStage.setScene(scene);
		
		primaryStage.setResizable(false);
		primaryStage.show();
		kioskcontroller = new Kioskcontroller("127.0.0.1", 50001, root);
		//kioskcontroller.connectServer();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
