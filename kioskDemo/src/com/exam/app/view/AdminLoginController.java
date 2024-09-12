package com.exam.app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.io.IOException;

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
        String password = passwordField.getText();

        if ("admin".equals(username) && "1234".equals(password)) {
            try {
                // Load the AdminMenuPopup.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminMenuPopup.fxml"));
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
    }
}
