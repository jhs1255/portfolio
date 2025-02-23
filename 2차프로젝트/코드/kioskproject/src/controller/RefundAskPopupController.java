package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RefundAskPopupController {

	@FXML
	private void handleCancelButtonAction(ActionEvent event) {
	    try {
	        // 현재 팝업 창을 닫기
	        Stage popupStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	        popupStage.close();

	        // 부모 창(Stage)을 가져와서 RefundList.fxml로 전환
	        Stage parentStage = (Stage) popupStage.getOwner();  // 팝업 창의 부모 창을 가져옴
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/4-6.RefundList.fxml"));
	        Parent refundListView = loader.load();

	        parentStage.setScene(new Scene(refundListView));
	        parentStage.show();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

    @FXML
    private void handleConfirmButtonAction(ActionEvent event) {
        try {
            // 현재 팝업 창을 닫기
            Stage popupStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            popupStage.close();

            // 4-2.RefundSuccessPopup 팝업을 띄우기
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/4-2.RefundSuccessPopup.fxml"));
            Parent successPopup = loader.load();

            // 새로운 팝업 창 생성
            Stage successStage = new Stage();
            successStage.initModality(Modality.APPLICATION_MODAL);  // 모달 설정
            successStage.setTitle("환불 성공");

            // 부모 창 설정
            Stage parentStage = (Stage) popupStage.getOwner();  // 부모 창을 가져옴
            successStage.initOwner(parentStage);  // 부모 창 설정

            successStage.setScene(new Scene(successPopup));
            successStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
