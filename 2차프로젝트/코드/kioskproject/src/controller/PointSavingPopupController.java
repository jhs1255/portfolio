package controller;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.MemberDto;
import dto.PlayInfoDto;
import dto.RequestCode;
import dto.RequestDto;
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

public class PointSavingPopupController {

    @FXML
    private TextField phoneField; // 휴대폰 번호 입력 필드

    @FXML
    private Button confirmButton; // 확인 버튼
    private buyController buyController;
    public PlayInfoDto playInfoDto;
	private ReservationDTO reserveDto;
	private MemberDto memberDto;

    private boolean isPlaceholderVisible = true; // 플레이스홀더가 표시되고 있는지 여부

    @FXML
    public void initialize() {
        // 처음에 확인 버튼 비활성화
        confirmButton.setDisable(true);
        confirmButton.setOnAction(e->{
        	HandleFunction handleFunction = new HandleFunction();
        	String phoneNumber = phoneField.getText();
        	System.out.println("Sending phone number: " + phoneNumber);
        	RequestDto requestDto = new RequestDto();
        	requestDto.setRequestCode(RequestCode.GET_PHONE_VAILD);
        	requestDto.setBody(phoneNumber);
        	String message = handleFunction.submit(requestDto);
        	ObjectMapper objectMapper = new ObjectMapper();
        	 try {
                 Map jsonMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
                 System.out.println("파싱된 JSON: " + jsonMap);
                 Integer statusCode = (Integer) jsonMap.get("statusCode");
                 System.out.println("상태 코드: " + statusCode);
                 if (statusCode == StatusCode.SUCCESS.getStatusCode()) {
                	 System.out.println("성공적인 응답을 받았습니다."); // 성공 메시지 추가
                     FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/1-2.PointSavingSuccessPopup.fxml"));
                     System.out.println("Before loading next page...");
                     Parent nextPage = loader.load();
                     System.out.println("Next page loaded.");
                     
                     PointSavingSuccessPopupController pointController = loader.getController();
                     pointController.setBuyController(buyController);
                     pointController.setPlayInfoDto(playInfoDto);
                     pointController.setMemberDto(memberDto);
                  // buyController에서 적립금을 가져와서 설정
                     buyController.savePoint();
                     int saving = buyController.getSavingPoints();
                     System.out.println("받아온 적립금"+saving);
                     pointController.setSavePoint(saving);
                     pointController.init();
                     
                     Stage stage = (Stage) confirmButton.getScene().getWindow();
                     stage.setScene(new Scene(nextPage));
                 } else if(statusCode == StatusCode.NOT_FOUND.getStatusCode()){
                	// 유효하지 않은 경우, NoReservation 팝업을 띄움
                     FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/1-3.PointSavingFailPopup.fxml"));
                     Parent noReservationRoot = loader.load();

                     Stage popupStage = new Stage();
                     popupStage.initModality(Modality.APPLICATION_MODAL);
                     popupStage.setTitle("회원 번호 없음");

                     Stage parentStage = (Stage) confirmButton.getScene().getWindow();
                     popupStage.initOwner(parentStage);

                     Scene scene = new Scene(noReservationRoot);
                     popupStage.setScene(scene);
                     popupStage.showAndWait();
                 }
             } catch (IOException e1) {
                 System.err.println("IOException occurred: " + e1.getMessage());
                 e1.printStackTrace();
             } catch (Exception e2) {
                 System.err.println("An unexpected error occurred: " + e2.getMessage());
                 e2.printStackTrace();
             }
        });

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
    
    
    public PlayInfoDto getPlayInfoDto() {
		return playInfoDto;
	}

	public void setPlayInfoDto(PlayInfoDto playInfoDto) {
		this.playInfoDto = playInfoDto;
	}

	public ReservationDTO getReserveDto() {
		return reserveDto;
	}

	public void setReserveDto(ReservationDTO reserveDto) {
		this.reserveDto = reserveDto;
	}

	public MemberDto getMemberDto() {
		return memberDto;
	}

	public void setMemberDto(MemberDto memberDto) {
		this.memberDto = memberDto;
	}

	public void setBuyController(buyController buyController) {
		this.buyController = buyController;
	    // 디버깅 로그 추가
	    if (this.buyController == null) {
	        System.err.println("buyController is null after setting.");
	    } else {
	        System.out.println("buyController set successfully.");
	    }
    }

	public buyController getBuyController() {
		return buyController;
	}
	
}
