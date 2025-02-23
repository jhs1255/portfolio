package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.RequestCode;
import dto.RequestDto;
import dto.ReservationDAO;
import dto.ReservationDTO;
import dto.StatusCode;
import handleMessage.HandleFunction;
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

public class ReservationPhoneController {

    @FXML
    private TextField phoneField; // 휴대폰 번호 입력 필드

    @FXML
    private Button searchButton; // 조회 버튼

    private boolean phoneFieldCleared = false; // 필드가 한 번 클릭되어 지워졌는지 여부
    
    private ReservationDAO reservationDAO = new ReservationDAO();

    // 유효한 전화번호 형식
    private static final String VALID_PHONE_NUMBER_PATTERN = "^010-\\d{4}-\\d{4}$"; // 010-XXXX-XXXX 형식

    @FXML
    public void initialize() {
        // 처음 시작할 때 버튼 비활성화
        searchButton.setDisable(true);

        // 입력 중에 하이픈 추가 및 유효성 검사
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 하이픈이 없는 상태로 숫자만 필터링
            String digitsOnly = newValue.replaceAll("[^\\d]", "");

            if (digitsOnly.length() > 11) {
                // 최대 11자리까지만 입력 가능
                digitsOnly = digitsOnly.substring(0, 11);
            }

            // 010-XXXX-XXXX 형식으로 변환
            StringBuilder formattedPhone = new StringBuilder(digitsOnly);
            if (formattedPhone.length() > 3) {
                formattedPhone.insert(3, "-");
            }
            if (formattedPhone.length() > 8) {
                formattedPhone.insert(8, "-");
            }

            // 최종 변환된 번호로 필드 업데이트 (재입력 방지)
            phoneField.setText(formattedPhone.toString());
            phoneField.positionCaret(formattedPhone.length()); // 커서를 항상 맨 끝에 유지

            // 유효한 형식인지 확인 후 버튼 활성화
            if (phoneField.getText().matches(VALID_PHONE_NUMBER_PATTERN)) {
                searchButton.setDisable(false); // 형식이 맞으면 버튼 활성화
            } else {
                searchButton.setDisable(true); // 형식이 맞지 않으면 버튼 비활성화
            }
        });
    }

    // 키패드에서 숫자 버튼 클릭 시 호출되는 메서드
    @FXML
    private void handleNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String number = clickedButton.getText();

        // 첫 입력 시 예시 텍스트 제거
        if (!phoneFieldCleared) {
            phoneField.clear();
            phoneFieldCleared = true;
        }

        // "지우기" 버튼 확인
        if ("지우기".equals(number)) {
            phoneField.clear(); // 전체 텍스트 삭제
        } else if ("X".equals(number)) {
            // 마지막 글자 삭제
            String currentText = phoneField.getText();
            if (!currentText.isEmpty()) {
                phoneField.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else {
            // 숫자 버튼 클릭 시 해당 숫자 추가
            phoneField.appendText(number);
        }
    }

    // 전화번호 입력창 클릭 시 플레이스홀더 제거
    @FXML
    private void clearPlaceholderPhone(MouseEvent event) {
        if (!phoneFieldCleared) {
            phoneField.clear(); // 플레이스홀더 제거
            phoneFieldCleared = true; // 이후로는 지우지 않음
        }
    }

    // "예매 번호로 조회" 버튼 클릭 시 ReservationNumber.fxml로 이동
    @FXML
    private void handleTicketSearchAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReservationNumber.fxml"));
            Parent reservationNumberView = loader.load();

            Stage stage = (Stage) phoneField.getScene().getWindow();
            Scene scene = new Scene(reservationNumberView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //입력된 번호에 따라 화면 전환
    @FXML
    private void handleReservationListAction(ActionEvent event) {
        String enteredPhoneNumber = phoneField.getText();

        if (enteredPhoneNumber.matches(VALID_PHONE_NUMBER_PATTERN)) {
        	// 서버에 요청할 DTO 생성
    	    RequestDto requestDto = new RequestDto();
    	    requestDto.setRequestCode(RequestCode.GET_PRINTPHONE_STRING);
    	    requestDto.setBody(enteredPhoneNumber);
    	    HandleFunction handleFunction = new HandleFunction();
    		String message = handleFunction.submit(requestDto);
    		System.out.println(message);
    		
    		ObjectMapper objectMapper = new ObjectMapper();
    		try {
    			Map<String, Object> jsonMap = null;
    			try {
    				jsonMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
    			} catch (JsonMappingException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();	
    			} catch (JsonProcessingException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}//try
    			//만약 성공했다면...
    			Integer statusCode = (Integer) jsonMap.get("statusCode");
    			if(statusCode == StatusCode.SUCCESS.getStatusCode()) {
    				List<ReservationDTO> list = null;
    				list = objectMapper.convertValue(jsonMap.get("body"), new TypeReference<List<ReservationDTO>>() {});
    				if (!list.isEmpty()) {
    	        		// 전화번호가 유효하면 ReservationList.fxml로 이동
    	                try {
    	                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/5-3.ReservationList.fxml"));
    	                    Parent reservationListView = loader.load();
    	                    ReservationListController controller = loader.getController();
    	                    if (list.size() == 1) {
    	                        controller.setReservationDetails(list); // 리스트로 전달
    	                    } else {
    	                        controller.setReservationDetails(list); // 여러 개의 예약일 때도 리스트로 전달
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
    			}
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
//        	List<ReservationDTO> reservations = reservationDAO.getReservationsByPhoneNumber(enteredPhoneNumber);
//        	if (!reservations.isEmpty()) {
//        		// 전화번호가 유효하면 ReservationList.fxml로 이동
//                try {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/5-3.ReservationList.fxml"));
//                    Parent reservationListView = loader.load();
//                    ReservationListController controller = loader.getController();
//                    if (reservations.size() == 1) {
//                        // 단일 예약일 경우
//                        controller.setReservationDetails(reservations.get(0).getRandomNumber());
//                    } else {
//                        // 여러 예약일 경우
//                        controller.setReservationDetails(reservations);
//                    }
//                    Stage stage = (Stage) phoneField.getScene().getWindow();
//                    Scene scene = new Scene(reservationListView);
//                    stage.setScene(scene);
//                    stage.show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        	}else {
//        		// 전화번호가 유효하지 않으면 Noreservation 팝업 띄움
//                try {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/0-3.Noreservation.fxml"));
//                    Parent noReservationRoot = loader.load();
//
//                    // 팝업 창 설정
//                    Stage popupStage = new Stage();
//                    popupStage.initModality(Modality.APPLICATION_MODAL);
//                    popupStage.setTitle("예매 내역 없음");
//
//                    // 부모 창 설정
//                    Stage parentStage = (Stage) phoneField.getScene().getWindow();
//                    popupStage.initOwner(parentStage);  // 부모 창을 팝업 창의 소유자로 설정
//
//                    Scene scene = new Scene(noReservationRoot);
//                    popupStage.setScene(scene);
//                    popupStage.showAndWait();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        	}

        }
    }
    
    //홈 버튼
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            // FXML 파일 로드 (kiosk.fxml로 이동)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
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
