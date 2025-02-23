package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

public class PointSavingFailController implements Initializable {

	@FXML
	public void handleRetryButtonAction(ActionEvent event) {
		// 현재 팝업 창을 닫음
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
	}
	
	@FXML
	public void handleCancelButtonAction(ActionEvent event) {
		try {
			// 현재 팝업 창을 닫음
            Stage popupStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            popupStage.close();
			// 팝업 창의 부모 스테이지(메인 스테이지)를 가져옴
	        Stage mainStage = (Stage) popupStage.getOwner();  // 팝업의 부모 스테이지
	        if (mainStage == null) {
	            // 부모 스테이지가 null이면 예외를 발생시킴
	            throw new NullPointerException("Parent stage is null");
	        }
	        mainStage.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		// 현재 팝업 창을 닫음
        Stage popupStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        popupStage.close();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
