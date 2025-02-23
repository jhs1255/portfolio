package controller;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.Admin;
import dto.RequestCode;
import dto.RequestDto;
import dto.StatusCode;
import handleMessage.HandleFunction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
    	String username = usernameField.getText();
        String pass = passwordField.getText();
        HandleFunction hf = new HandleFunction();
        RequestDto rd = new RequestDto();
        rd.setRequestCode(RequestCode.POST_LOGIN_IDANDPASS);
        Admin admin = new Admin();
        admin.setId(username);
        admin.setPassword(pass);
        rd.setBody(admin);

        String returnValue = hf.submit(rd);
        if (returnValue == null || returnValue.isEmpty()) {
            errorLabel.setText("서버 응답이 없습니다.");
            return;
        }

        ObjectMapper obm = new ObjectMapper();
        Map<String, Object> jsonMap = null;
        boolean isCorrect = false;

        try {
            jsonMap = obm.readValue(returnValue, new TypeReference<Map<String, Object>>() {});
            if (jsonMap != null && jsonMap.containsKey("statusCode")) {
            	Object statusCodeObj = jsonMap.get("statusCode");
                if (statusCodeObj != null) {
                    int statusCode = Integer.parseInt(statusCodeObj.toString());
                    isCorrect = (statusCode == StatusCode.SUCCESS.getStatusCode());
                } else {
                    errorLabel.setText("상태 코드가 null입니다.");
                }
            } else {
                errorLabel.setText("상태 코드를 찾을 수 없습니다.");
            }
        } catch (JsonMappingException e) {
            errorLabel.setText("서버 응답 형식이 잘못되었습니다.");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            errorLabel.setText("응답 처리 중 오류가 발생했습니다.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            errorLabel.setText("상태 코드 변환 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        if (isCorrect) {
            try {
                // Load the AdminMenuPopup.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminMenuPopup.fxml"));
                Parent menuPopup = loader.load();

                // Get the Stage from the current scene and set the new scene
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(menuPopup));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    
//        if (username.equals("admin") && password.equals("1234")) {
//            try {
//                // Load the AdminMenuPopup.fxml
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminMenuPopup.fxml"));
//                Parent menuPopup = loader.load();
//
//                // Get the Stage from the current scene and set the new scene
//                Stage stage = (Stage) loginButton.getScene().getWindow();
//                stage.setScene(new Scene(menuPopup));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            errorLabel.setText("아이디 또는 비밀번호가 잘못되었습니다.");
//        }
    }
}
