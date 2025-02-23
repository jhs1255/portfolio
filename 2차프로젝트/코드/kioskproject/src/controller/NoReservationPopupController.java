package controller;
 
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class NoReservationPopupController {

    // "재입력" 버튼 클릭 시 팝업을 닫음
    @FXML
    private void handleRetryButtonAction(ActionEvent event) {
        // 현재 팝업 창을 닫음
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
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

            // 홈 화면의 FXML 파일을 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
            Parent homeView = loader.load();

            // 메인 스테이지의 씬을 변경
            Scene scene = new Scene(homeView);
            mainStage.setScene(scene);
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace(); // 에러 발생 시 스택 트레이스 출력
        } catch (NullPointerException e) {
            e.printStackTrace(); // 부모 스테이지가 null일 경우 스택 트레이스 출력
        }
    }

}
