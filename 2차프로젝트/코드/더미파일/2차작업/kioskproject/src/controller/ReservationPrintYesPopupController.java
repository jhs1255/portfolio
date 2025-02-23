package controller;

import javafx.event.ActionEvent; 
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReservationPrintYesPopupController {

    @FXML
    private void handleConfirmButtonAction(ActionEvent event) {
        try {
            // 팝업 창 닫기
            Stage popupStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            popupStage.close();  // 팝업 창을 닫음

            // kiosk.fxml 파일을 로드
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
            Parent root = fxmlLoader.load();

            // 기존 메인 창(Stage)을 가져와서 새로운 Scene으로 전환
            Stage mainStage = (Stage) popupStage.getOwner();  // 팝업 창의 부모 창을 가져옴
            mainStage.setScene(new Scene(root));
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
