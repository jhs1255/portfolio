package com.exam.app.view;

import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservationNumberController {

    @FXML
    private TextField phoneField; // FXML 파일에 있는 TextField에 연결

    @FXML
    private Button searchButton; // 조회 버튼

    private boolean isPlaceholderCleared = false; // Placeholder가 지워졌는지 여부 확인
    
    @FXML
    private void handlePhoneSearchAction(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 휴대폰 번호로 조회하는 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/ReservationPhone.fxml"));
            Parent phoneSearchView = loader.load();

            // 현재 스테이지 가져오기
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // 새로운 씬으로 전환
            Scene scene = new Scene(phoneSearchView);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 임시로 설정된 예매 번호
    private static final String VALID_RESERVATION_NUMBER = "123456789123456";

    @FXML
    public void initialize() {
        // 처음 시작할 때 버튼 비활성화
        searchButton.setDisable(true);

        // TextField의 텍스트가 변경될 때마다 호출되는 리스너 추가
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 텍스트가 15자리인지 확인하고 버튼 활성화 여부 결정
            if (newValue.length() == 15) {
                searchButton.setDisable(false); // 버튼 활성화
            } else {
                searchButton.setDisable(true); // 버튼 비활성화
            }
        });
    }

    @FXML
    private void clearPlaceholderPhone(MouseEvent event) {
        phoneField.clear(); // Placeholder를 지우는 로직
    }
    
    // 번호 버튼 클릭 시 호출되는 메서드
    @FXML
    private void handleNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        // 처음 숫자를 클릭할 때 placeholder를 지움
        if (!isPlaceholderCleared) {
            phoneField.clear(); // 입력 필드 초기화
            isPlaceholderCleared = true; // 한 번 지운 후에는 더 이상 지우지 않음
        }

        // "X" 버튼 처리
        if (buttonText.equals("X")) {
            String currentText = phoneField.getText();
            if (!currentText.isEmpty()) {
                phoneField.setText(currentText.substring(0, currentText.length() - 1)); // 마지막 글자 삭제
            }
        }
        // "지우기" 버튼 처리
        else if (buttonText.equals("지우기")) {
            phoneField.clear(); // 입력 필드 초기화
            isPlaceholderCleared = false; // placeholder를 다시 표시 가능하게 함
        }
        // 숫자 버튼 처리
        else {
            phoneField.appendText(buttonText); // 클릭한 버튼의 텍스트 추가
        }
    }

    // 티켓 조회 버튼 클릭 시 호출되는 메서드
    @FXML
    private void handleTicketSearchAction(ActionEvent event) {
        String enteredReservationNumber = phoneField.getText();
        if (enteredReservationNumber.equals(VALID_RESERVATION_NUMBER)) {
            // 예매 번호가 올바르면 ReservationList.fxml로 이동
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/ReservationList.fxml"));
                Parent reservationListRoot = loader.load();

                // 현재 스테이지 가져오기
                Stage stage = (Stage) phoneField.getScene().getWindow();

                // 새로운 씬으로 전환
                Scene scene = new Scene(reservationListRoot);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace(); // 에러 발생 시 콘솔에 출력
            }
        } else {
            // 예매 번호가 잘못되면 팝업 표시
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/0-3.Noreservation.fxml"));
                Parent noReservationRoot = loader.load();

                // 팝업 창 설정
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL); // 모달로 설정
                popupStage.initOwner(((Stage) phoneField.getScene().getWindow())); // 부모 스테이지 설정

                // 팝업 타이틀 및 씬 설정
                popupStage.setTitle("예매 내역 없음");
                Scene scene = new Scene(noReservationRoot);
                popupStage.setScene(scene);

                // 팝업 창을 띄움 (모달)
                popupStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace(); // 에러 발생 시 콘솔에 출력
            }
        }
    }

    //홈 버튼
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            // FXML 파일 로드 (kiosk.fxml로 이동)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/kiosk.fxml"));
            Parent homeView = loader.load();

            // 현재 스테이지 가져오기
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // 새로운 씬으로 전환
            Scene scene = new Scene(homeView);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // 에러 발생 시 콘솔에 출력
        }
    }

}
