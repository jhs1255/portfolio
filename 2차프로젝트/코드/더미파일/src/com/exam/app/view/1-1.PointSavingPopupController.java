package com.exam.app.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PointSavingPopupController {

    @FXML
    private TextField phoneField; // 휴대폰 번호 입력 필드

    @FXML
    private Button confirmButton; // 확인 버튼

    private boolean isPlaceholderVisible = true; // 플레이스홀더가 표시되고 있는지 여부

    @FXML
    public void initialize() {
        // 처음에 확인 버튼 비활성화
        confirmButton.setDisable(true);

        // 전화번호 입력 필드에 리스너 추가하여 '-' 추가 및 형식 확인
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 숫자만 입력하도록 처리
            String digits = newValue.replaceAll("[^\\d]", "");
            
            // 하이픈을 추가하여 010-XXXX-XXXX 형식 만들기
            if (digits.length() > 3 && digits.length() <= 7) {
                phoneField.setText(digits.substring(0, 3) + "-" + digits.substring(3));
            } else if (digits.length() > 7) {
                phoneField.setText(digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7));
            } else {
                phoneField.setText(digits); // 그 외의 경우는 하이픈 없이 숫자만 출력
            }
            
            // 커서를 항상 맨 끝에 위치시키기
            phoneField.positionCaret(phoneField.getText().length());

            // 입력된 값이 정확한 형식인지 확인
            if (phoneField.getText().matches("010-\\d{4}-\\d{4}")) {
                confirmButton.setDisable(false); // 형식이 맞으면 버튼 활성화
            } else {
                confirmButton.setDisable(true); // 형식이 맞지 않으면 버튼 비활성화
            }
        });
    }

    // 숫자 키패드 버튼 클릭 시 호출되는 메서드
    @FXML
    private void handleNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource(); // 클릭된 버튼
        String buttonText = clickedButton.getText(); // 버튼의 텍스트(숫자)

        // 처음 클릭할 때 예시 텍스트를 지움
        if (isPlaceholderVisible) {
            phoneField.clear(); // 입력 필드를 비움
            isPlaceholderVisible = false;
        }

        // "X" 버튼일 때 처리
        if ("X".equals(buttonText)) {
            String currentText = phoneField.getText();
            if (!currentText.isEmpty()) {
                phoneField.setText(currentText.substring(0, currentText.length() - 1)); // 마지막 글자 삭제
            }
        }
        // "지우기" 버튼일 때 전체 삭제
        else if ("지우기".equals(buttonText)) {
            phoneField.clear(); // 입력 필드 전체 삭제
        }
        // 그 외 숫자 버튼 처리
        else {
            phoneField.appendText(buttonText); // 클릭된 숫자를 입력 필드에 추가
        }
    }

    // 입력 필드를 클릭하면 예시 텍스트를 제거하는 메서드
    @FXML
    private void clearPlaceholder(MouseEvent event) {
        if (isPlaceholderVisible) {
            phoneField.clear(); // 예시 텍스트를 비움
            isPlaceholderVisible = false; // 이후로는 지우지 않음
        }
    }
}
