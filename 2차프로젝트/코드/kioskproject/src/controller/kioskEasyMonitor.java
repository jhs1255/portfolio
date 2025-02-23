package controller;

import java.net.URL;
import java.util.ResourceBundle;

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
import view.AppMain;

//EasyMonitor랑 호환
public class kioskEasyMonitor implements Initializable{
    @FXML private MenuItem admin;
	@FXML private MenuItem unconnect;
	
	@FXML private StackPane mainpane;
	@FXML private Button buyTicket; //티켓 구매 버튼
	@FXML private Button printTicket;//예매 조회 및 티켓출력 버튼
	@FXML private Button cancelTicket;// 티켓 취소 버튼
	@FXML private Button BackBtn; // 이전버튼

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        if(admin != null){
            admin.setOnAction(event->connectAdmin(event));// 관리자 버튼
        }
        if(unconnect != null){
		    unconnect.setOnAction(event->UNConnect(event));//서버 접속 해제
        }
        if(buyTicket != null){
		    buyTicket.setOnAction(event->choise(event));//티켓 구매 버튼
        }
        if(printTicket != null){
            printTicket.setOnAction(event->printTk(event));//예매 조회 및 티켓 출력 버튼
        }
		if(cancelTicket != null){
            cancelTicket.setOnAction(event->cancelTk(event));//티켓 취소 화면
        }
        if(BackBtn != null){
		    BackBtn.setOnAction(event->BackHome(event)); //이전 화면으로
        }
    }
    public void BackHome(ActionEvent event) {
		try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
            Parent homeRoot = loader.load();
            // 현재 창의 Stage를 가져옴
            Stage stage = (Stage) BackBtn.getScene().getWindow();
            Scene scene = new Scene(homeRoot); // 홈화면으로 전환

            // 메뉴 화면으로 전환
            stage.setScene(scene);
            stage.show(); //화면에 출력s

        } catch (Exception e) {
            e.printStackTrace();
            // 예외 발생 시 사용자에게 경고창 표시
        }
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
            
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void UNConnect(ActionEvent e) {
		AppMain.kioskcontroller.closeServer();
	}
	
	public void choise(ActionEvent event) {
		try {
            System.out.println("예매버튼클릭");
			Parent moviestage = (Parent)FXMLLoader.load(getClass().getResource("/fxml/EasyDate.fxml"));
			StackPane root = (StackPane) mainpane.getScene().getRoot();
			root.getChildren().add(moviestage);
			
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
}
