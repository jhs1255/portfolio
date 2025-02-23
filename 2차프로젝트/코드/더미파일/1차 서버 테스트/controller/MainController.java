package kioskdemo;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MainController implements Initializable {
	@FXML Button buyTicket;
	@FXML Button printTicket;
	@FXML Button cancelTicket;
	@FXML Button changeScreen;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		buyTicket.setOnAction(event -> parchaseTicket(event)); // 티켓 구매 버튼 누를 시
		printTicket.setOnAction(event -> printTK(event)); // 티켓 출력 버튼 누를 시
		cancelTicket.setOnAction(event -> cancelTK(event)); // 티켓 취소 버튼 누를 시
		changeScreen.setOnAction(event -> changeSC(event));
	}

	public void parchaseTicket(ActionEvent event) { //티켓 구매 기능
		System.out.println("티켓구매 클릭");
	}
	
	public void printTK(ActionEvent event) {// 티켓 조회 및 출력
		System.out.println("티켓출력 클릭");
	}
	
	public void cancelTK(ActionEvent event) {// 티켓 취소 및 환불
		System.out.println("티켓취소 클릭");
	}
	
	public void changeSC(ActionEvent event) {// 쉬운화면 전환
		System.out.println("쉬운화면보기 클릭");
	}
	
	public void connectServer(ActionEvent e) { // 서버 연결 버튼
		kiosk.kioskcontroller.connectServer();
	}
	
	public void closeServer(ActionEvent e) { // 서버 종료 버튼
		kiosk.kioskcontroller.closeServer();
	}
	
}
