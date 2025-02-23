package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RefundSuccessPopupController {

    @FXML
    private void handleConfirmButtonAction(ActionEvent event) {
        try {
            // 현재 팝업 창을 닫기
            Stage popupStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            popupStage.close();

            // kiosk.fxml 파일을 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
            Parent homeView = loader.load();

            // 부모 창(Stage)을 가져와서 새로운 Scene으로 전환
            Stage parentStage = (Stage) popupStage.getOwner();  // 팝업 창의 부모 창을 가져옴
            parentStage.setScene(new Scene(homeView));
            parentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
