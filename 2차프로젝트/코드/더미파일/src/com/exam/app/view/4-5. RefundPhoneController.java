package com.exam.app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class RefundPhoneController {

    @FXML
    private TextField phoneField; // 휴대폰 번호 입력 필드
    @FXML
    private Button ticketSearchButton; // 티켓 조회 버튼

    private boolean isPlaceholder = true; // 예제 텍스트 상태 확인 변수

    // 키패드 버튼 클릭 시 호출되는 메서드
    @FXML
    private void handleNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        // 예제 텍스트가 남아있다면 초기화
        if (isPlaceholder) {
            phoneField.setText("");
            isPlaceholder = false;
        }

        // "지우기" 버튼 처리
        if (buttonText.equals("지우기")) {
            phoneField.clear(); // 입력 필드 전체 초기화
        }
        // "X" 버튼 처리
        else if (buttonText.equals("X")) {
            String currentText = phoneField.getText();
            if (!currentText.isEmpty()) {
                phoneField.setText(currentText.substring(0, currentText.length() - 1)); // 마지막 글자 삭제
            }
        }
        // 숫자 버튼 처리
        else {
            phoneField.appendText(buttonText); // 클릭한 버튼의 숫자 입력
        }

        // 전화번호 형식에 맞게 "-" 자동 삽입
        formatPhoneNumber();
    }

    // 입력 필드 클릭 시 예제 텍스트를 제거하는 메서드
    @FXML
    private void clearPlaceholderPhone(MouseEvent event) {
        if (isPlaceholder) {
            phoneField.setText("");
            isPlaceholder = false;
        }
    }

    // 전화번호 형식에 맞게 자동으로 "-" 삽입
    private void formatPhoneNumber() {
        String currentText = phoneField.getText().replaceAll("-", "");
        StringBuilder formatted = new StringBuilder();

        if (currentText.length() >= 3) {
            formatted.append(currentText.substring(0, 3));
            if (currentText.length() > 3) {
                formatted.append("-");
                if (currentText.length() >= 7) {
                    formatted.append(currentText.substring(3, 7));
                    if (currentText.length() > 7) {
                        formatted.append("-");
                        formatted.append(currentText.substring(7, Math.min(11, currentText.length())));
                    }
                } else {
                    formatted.append(currentText.substring(3));
                }
            }
        } else {
            formatted.append(currentText);
        }

        phoneField.setText(formatted.toString());
        checkPhoneNumberFormat();
    }

    // 전화번호 형식이 맞는지 확인하여 버튼 활성화/비활성화
    private void checkPhoneNumberFormat() {
        String phoneNumber = phoneField.getText();
        // 정규식으로 "010-XXXX-XXXX" 형식 확인
        if (phoneNumber.matches("010-\\d{4}-\\d{4}")) {
            ticketSearchButton.setDisable(false); // 형식이 맞으면 버튼 활성화
        } else {
            ticketSearchButton.setDisable(true); // 형식이 맞지 않으면 버튼 비활성화
        }
    }

    @FXML
    private void handleTicketSearchAction(ActionEvent event) {
        String phoneNumber = phoneField.getText();

        if (phoneNumber.equals("010-1234-5678")) {
            // 번호가 맞으면 RefundList.fxml로 이동
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/RefundList.fxml"));
                Parent refundListView = loader.load();

                Stage stage = (Stage) phoneField.getScene().getWindow();
                Scene scene = new Scene(refundListView);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 번호가 맞지 않으면 0-3.NoReservation 팝업 표시
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/0-3.NoReservation.fxml"));
                Parent popup = loader.load();

                Stage popupStage = new Stage();
                popupStage.setTitle("조회 실패");

                // 현재 창을 부모 스테이지로 설정
                Stage mainStage = (Stage) phoneField.getScene().getWindow();
                popupStage.initOwner(mainStage);

                popupStage.setScene(new Scene(popup));
                popupStage.showAndWait(); // 팝업이 표시된 동안 다른 작업을 막음
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    // 홈 버튼 클릭 시 kiosk.fxml로 이동
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/kiosk.fxml"));
            Parent homeView = loader.load();

            Stage stage = (Stage) phoneField.getScene().getWindow();
            Scene scene = new Scene(homeView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 예매번호로 조회 버튼 클릭 시 RefundNumber.fxml로 이동
    @FXML
    private void handleReservationNumberSearchAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/RefundNumber.fxml"));
            Parent numberSearchView = loader.load();

            Stage stage = (Stage) phoneField.getScene().getWindow();
            Scene scene = new Scene(numberSearchView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
