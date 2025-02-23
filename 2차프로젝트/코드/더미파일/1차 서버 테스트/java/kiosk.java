package kioskdemo;

import kioskdemo.Kioskcontroller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class kiosk extends Application {

	public static Kioskcontroller kioskcontroller = null;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		Scene scene = new Scene(root,700,800); // 크기 가로 700 세로 800
		
		primaryStage.setTitle("데모파일");
		primaryStage.setScene(scene);
		
		primaryStage.setResizable(false); // 화면 크기 고정
		primaryStage.show();
		kioskcontroller = new Kioskcontroller("127.0.0.1", 50001, root); //서버와 연결하기 위한 컨트롤러입니다. IP = localhost, port=50001
		kioskcontroller.connectServer(); // 처음 실행 시 서버와 자동 접속하게 해주는 부분
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}
