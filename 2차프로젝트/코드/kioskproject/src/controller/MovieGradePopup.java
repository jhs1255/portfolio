package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MovieGradePopup implements Initializable {
	@FXML private Button okCheckBtn;//확인버튼
	
	private choiseMovieController parentController; // 부모 컨트롤러 저장

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 버튼 클릭 시 처리할 메서드 연결
        okCheckBtn.setOnAction(event -> handleOkButtonClick());
	}
	
	public void setParentController(choiseMovieController parentController) {
        this.parentController = parentController; // 부모 컨트롤러 설정
    }
	
	private void handleOkButtonClick() {
        // 이전 페이지의 proceedNextPage 메서드 호출
        if (parentController != null) {
            parentController.proceedNextPage(); // 부모 컨트롤러의 메서드 호출
        }

        // 팝업 창 닫기
        Stage stage = (Stage) okCheckBtn.getScene().getWindow();
        stage.close();
    }
	
}
