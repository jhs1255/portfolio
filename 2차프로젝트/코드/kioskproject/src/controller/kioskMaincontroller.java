package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.AppMain;

public class kioskMaincontroller implements Initializable {
	@FXML private MenuItem admin;
	@FXML private MenuItem unconnect;
	
	@FXML private StackPane mainpane;
	@FXML private Button buyTicket; //티켓 구매 버튼
	@FXML private Button printTicket;//예매 조회 및 티켓출력 버튼
	@FXML private Button cancelTicket;// 티켓 취소 버튼
	@FXML private Button EasyMonitorBtn;//쉬운 화면보기

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		admin.setOnAction(event->connectAdmin(event));// 관리자 버튼
		unconnect.setOnAction(event->UNConnect(event));//서버 접속 해제
		buyTicket.setOnAction(event->choise(event));//티켓 구매 버튼
		printTicket.setOnAction(event->printTk(event));//예매 조회 및 티켓 출력 버튼
		cancelTicket.setOnAction(event->cancelTk(event));//티켓 취소 화면
		EasyMonitorBtn.setOnAction(event->easyChange(event));
	}
	
	public void connectAdmin(ActionEvent event) {
		System.out.println("관리자 접속 클릭");
		try {
			//main을 stackpane으로 생성하였기 때문에 아래 주석을 해제하면 키오스크 화면에 관리자 화면이 바로 뜨는 방식입니다.
			
			//Parent adminstage = (Parent)FXMLLoader.load(getClass().getResource("/fxml/AdminLoginPopup.fxml"));
			/*StackPane root = (StackPane) mainpane.getScene().getRoot();
			root.getChildren().add(adminstage);*/
			
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("/fxml/AdminLoginPopup.fxml"));
			Stage adminStage = new Stage();
			adminStage.setScene(new Scene(root));
            adminStage.setTitle("키오스크-관리자");
            adminStage.show();
            
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void UNConnect(ActionEvent e) {
		AppMain.kioskcontroller.closeServer();
	}
	
	public void choise(ActionEvent event) {
		try {
			Parent moviestage = (Parent)FXMLLoader.load(getClass().getResource("/fxml/choiseMovie.fxml"));
			StackPane root = (StackPane) mainpane.getScene().getRoot();
			root.getChildren().add(moviestage);
			
			moviestage.setTranslateX(650);
			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(moviestage.translateXProperty(), 0);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(200),keyValue);
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();
			
			Scene scene = mainpane.getScene();
			scene.getStylesheets().add(getClass().getResource("/fxml/css/choiseMovie.css").toExternalForm());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//예매 티켓 조회 및 출력
	public void printTk(ActionEvent event) {
		try {
			Parent resPrintstage = (Parent)FXMLLoader.load(getClass().getResource("/fxml/ReservationNumber.fxml"));
			StackPane root = (StackPane) mainpane.getScene().getRoot();
			root.getChildren().add(resPrintstage);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//예매 티켓 취소
	public void cancelTk(ActionEvent event) {
		try {
			Parent refundPrintstage = (Parent)FXMLLoader.load(getClass().getResource("/fxml/4-4.RefundNumber.fxml"));
			StackPane root = (StackPane) mainpane.getScene().getRoot();
			root.getChildren().add(refundPrintstage);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//쉬운화면 전환
	public void easyChange(ActionEvent event) {
		try {
			Parent refundPrintstage = (Parent)FXMLLoader.load(getClass().getResource("/fxml/EasyMonitor.fxml"));
			StackPane root = (StackPane) mainpane.getScene().getRoot();
			root.getChildren().add(refundPrintstage);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
