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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.IOException;

public class RefundNumberController {

    @FXML
    private TextField phoneField; // FXML 파일에 있는 TextField에 연결

    @FXML
    private Button searchButton; // 버튼을 FXML에서 연결

    private boolean isPlaceholder = true; // 플레이스홀더 상태
    private static final String VALID_RESERVATION_NUMBER = "123456789123456"; // 임의로 설정한 유효한 예매번호

    @FXML
    public void initialize() {
        // TextField의 텍스트가 변경될 때마다 호출되는 리스너 추가
        phoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 텍스트가 15자리인지 확인하고 버튼 활성화 여부 결정
                if (newValue.length() == 15) {
                    searchButton.setDisable(false); // 버튼 활성화
                } else {
                    searchButton.setDisable(true); // 버튼 비활성화
                }
            }
        });

        // 처음 시작할 때 버튼 비활성화
        searchButton.setDisable(true);
    }

    // 번호 버튼 클릭 시 호출되는 메서드
    @FXML
    private void handleNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        if (isPlaceholder) {
            phoneField.setText(""); // 플레이스홀더 텍스트 삭제
            isPlaceholder = false;
        }

        // "지우기" 버튼 처리
        if (buttonText.equals("X")) {
            String currentText = phoneField.getText();
            if (!currentText.isEmpty()) {
                phoneField.setText(currentText.substring(0, currentText.length() - 1)); // 마지막 글자 삭제
            }
        }
        // "지우기" 버튼 처리
        else if (buttonText.equals("지우기")) {
            phoneField.clear(); // 입력 필드 초기화
        }
        // 숫자 버튼 처리
        else {
            phoneField.appendText(buttonText); // 클릭한 버튼의 텍스트 추가
        }
    }

    // 입력 필드 클릭 시 placeholder를 제거하는 메서드
    @FXML
    private void clearPlaceholderPhone(MouseEvent event) {
        if (isPlaceholder) {
            phoneField.setText(""); // 입력 필드를 비움
            isPlaceholder = false;
        }
    }

    // 티켓 조회 버튼 클릭 시 호출되는 메서드
    @FXML
    private void handleTicketSearchAction(ActionEvent event) {
        String enteredReservationNumber = phoneField.getText();

        if (enteredReservationNumber.equals(VALID_RESERVATION_NUMBER)) {
            // 예매 번호가 유효할 경우 RefundList.fxml로 이동
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/RefundList.fxml"));
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
            // 예매 번호가 유효하지 않을 경우 0-3.Noreservation 팝업 창 띄우기
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/0-3.Noreservation.fxml"));
                Parent noReservationRoot = loader.load();

                // 팝업 창 설정
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("예매 내역 없음");

                // 부모 창 설정
                Stage parentStage = (Stage) phoneField.getScene().getWindow();
                popupStage.initOwner(parentStage);  // 부모 창을 팝업 창의 소유자로 설정

                Scene scene = new Scene(noReservationRoot);
                popupStage.setScene(scene);
                popupStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace(); // 에러 발생 시 콘솔에 출력
            }
        }
    }

    // "휴대폰 번호로 조회" 버튼 클릭 시 호출되는 메서드
    @FXML
    private void handlePhoneSearchAction(ActionEvent event) {
        try {
            // RefundPhoneController가 연결된 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/RefundPhone.fxml"));
            Parent phoneSearchView = loader.load();

            // 현재 스테이지 가져오기
            Stage stage = (Stage) phoneField.getScene().getWindow();

            // 새로운 씬으로 전환
            Scene scene = new Scene(phoneSearchView);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // 에러 발생 시 콘솔에 출력
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
