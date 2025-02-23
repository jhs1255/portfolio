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
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReservationNumberController {
	@FXML StackPane ResNumPane;
	@FXML Button home;
	
    @FXML
    private TextField phoneField; // FXML 파일에 있는 TextField에 연결

    @FXML
    private Button searchButton; // 조회 버튼

    private boolean isPlaceholderCleared = false; // Placeholder가 지워졌는지 여부 확인
    
    @FXML
    private void handlePhoneSearchAction(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 휴대폰 번호로 조회하는 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/5-2.ReservationPhone.fxml"));
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
    
    
    private ReservationDAO reservationDAO = new ReservationDAO(); // DAO 객체 생성

    @FXML
    public void initialize() {
        // 처음 시작할 때 버튼 비활성화
        searchButton.setDisable(true);

        // TextField의 텍스트가 변경될 때마다 호출되는 리스너 추가
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 텍스트가 15자리인지 확인하고 버튼 활성화 여부 결정
            if (newValue.length() == 16) {
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
        RequestDto requestDto = new RequestDto();
	    requestDto.setRequestCode(RequestCode.GET_PRINTRESNUM_STRING);
	    requestDto.setBody(enteredReservationNumber);
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
//        if (reservationDAO.isReservationNumberValid(enteredReservationNumber)) {
//            // 예매 번호가 올바르면 ReservationList.fxml로 이동
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/5-3.ReservationList.fxml"));
//                Parent reservationListRoot = loader.load();
//                // 컨트롤러에 접근하여 메서드를 호출하여 데이터 전달
//                ReservationListController controller = loader.getController();
//                controller.setReservationDetails(enteredReservationNumber);  // 예매번호를 넘겨줌
//                // 현재 스테이지 가져오기
//                Stage stage = (Stage) phoneField.getScene().getWindow();
//
//                // 새로운 씬으로 전환
//                Scene scene = new Scene(reservationListRoot);
//                stage.setScene(scene);
//                stage.show();
//
//            } catch (IOException e) {
//                e.printStackTrace(); // 에러 발생 시 콘솔에 출력
//            }
//        } else {
//            // 예매 번호가 잘못되면 팝업 표시
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/0-3.Noreservation.fxml"));
//                Parent noReservationRoot = loader.load();
//
//                // 팝업 창 설정
//                Stage popupStage = new Stage();
//                popupStage.initModality(Modality.APPLICATION_MODAL); // 모달로 설정
//                popupStage.initOwner(((Stage) phoneField.getScene().getWindow())); // 부모 스테이지 설정
//
//                // 팝업 타이틀 및 씬 설정
//                popupStage.setTitle("예매 내역 없음");
//                Scene scene = new Scene(noReservationRoot);
//                popupStage.setScene(scene);
//
//                // 팝업 창을 띄움 (모달)
//                popupStage.showAndWait();
//
//            } catch (IOException e) {
//                e.printStackTrace(); // 에러 발생 시 콘솔에 출력
//            }
//        }
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
