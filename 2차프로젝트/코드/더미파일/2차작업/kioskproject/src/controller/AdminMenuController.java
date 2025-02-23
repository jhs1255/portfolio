package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.AppMain;
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.io.IOException;

public class AdminMenuController {

    @FXML
    private Button movieRegistrationButton;

    @FXML
    private Button screeningInfoButton;

    @FXML
    private Button salesCheckButton;

    @FXML
    private Button exitButton;

    @FXML
    private void handleMovieRegistration() {
        switchScene("/fxml/MovieRegister.fxml");
    }

    @FXML
    private void handleScreeningInfo() {
        switchScene("/fxml/MovieSetting.fxml");
    }

    @FXML
    private void handleSalesCheck() {
        switchScene("/fxml/SalesCheck.fxml");
    }

    @FXML
    private void handleExit() {
        // 종료 확인을 위한 알림 생성
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("종료 확인");
        alert.setHeaderText(null);
        alert.setContentText("관리자 페이지를 종료하시겠습니까?");

        // 확인 및 취소 버튼 추가
        ButtonType confirmButton = new ButtonType("확인");
        ButtonType cancelButton = new ButtonType("취소");
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        // 알림을 표시하고 사용자 응답 대기
        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                // 사용자가 확인을 클릭하면 애플리케이션 종료
                Stage stage = (Stage) exitButton.getScene().getWindow();
                stage.close();
            }
            // 사용자가 취소를 클릭하면 아무 동작도 하지 않음
        });
    }

    private void switchScene(String fxmlFile) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newScene = loader.load();

            // Get the Stage from the current scene and set the new scene
            Stage stage = (Stage) movieRegistrationButton.getScene().getWindow();
            stage.setScene(new Scene(newScene));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
