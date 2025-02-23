package controller;

import java.io.IOException;
import java.util.List;

import dto.ReservationDAO;
import dto.ReservationDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RefundPhoneController {

    @FXML
    private TextField phoneField; // 휴대폰 번호 입력 필드
    @FXML
    private Button ticketSearchButton; // 티켓 조회 버튼

    private boolean isPlaceholder = true; // 예제 텍스트 상태 확인 변수
    
    private ReservationDAO reservationDAO = new ReservationDAO();
    private static final String VALID_PHONE_NUMBER_PATTERN = "^010-\\d{4}-\\d{4}$";

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

        if (phoneNumber.matches(VALID_PHONE_NUMBER_PATTERN)) {
        	List<ReservationDTO> reservations = reservationDAO.getReservationsByPhoneNumber(phoneNumber);
        	if (!reservations.isEmpty()) {
        		// 전화번호가 유효하면 ReservationList.fxml로 이동
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/4-6.RefundList.fxml"));
                    Parent reservationListView = loader.load();
                    RefundListController controller = loader.getController();
                    if (reservations.size() == 1) {
                        // 단일 예약일 경우
                        controller.setReservationDetails(reservations.get(0).getRandomNumber());
                    } else {
                        // 여러 예약일 경우
                        controller.setReservationDetails(reservations);
                    }
                    Stage stage = (Stage) phoneField.getScene().getWindow();
                    Scene scene = new Scene(reservationListView);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        	}else {
        		// 전화번호가 유효하지 않으면 Noreservation 팝업 띄움
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/0-3.Noreservation.fxml"));
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
                    e.printStackTrace();
                }
        	}
        	
        	
        } else {
            // 전화번호가 유효하지 않으면 Noreservation 팝업 띄움
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/0-3.Noreservation.fxml"));
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
                e.printStackTrace();
            }
        }
    }



    // 홈 버튼 클릭 시 kiosk.fxml로 이동
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/4-4.RefundNumber.fxml"));
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
