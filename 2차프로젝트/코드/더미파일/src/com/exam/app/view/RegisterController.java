package com.exam.app.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RegisterController {

    // MovieRegister.fxml에 대한 필드
    @FXML
    private VBox movieListVBox;

    @FXML
    private Button registerButton;

    @FXML
    private ScrollPane scrollPane;

    // MovieRegisterPopup.fxml에 대한 필드
    @FXML
    private TextField titleField;

    @FXML
    private TextField runtimeField;

    @FXML
    private TextField ratingField;
    
    @FXML
    private ComboBox<String> kindComboBox;

    @FXML
    private Button submitButton;
    
    @FXML
    private Button backToMenuButton;

    // 현재 선택된 HBox와 영화 제목을 추적하는 변수
    private HBox selectedMovieBox;
    private String selectedMovieTitle;  // 선택된 영화의 제목을 저장할 변수

    // MovieRegister.fxml에서 영화 데이터를 로드하기 위한 초기화 메서드
    public void initialize() {
        if (movieListVBox == null) {
            System.out.println("movieListVBox is null");
        } else {
            loadMovieData();
        }
    }

    // VBox에 샘플 영화 데이터를 로드하는 메서드 (MovieRegister.fxml용)
    private void loadMovieData() {
        for (int i = 1; i <= 10; i++) {
            HBox movieBox = new HBox(10);
            movieBox.setPrefHeight(100);
            movieBox.setPrefWidth(200);

            ImageView movieImage = new ImageView();
            movieImage.setFitHeight(140);
            movieImage.setFitWidth(100);
            //movieImage.setImage(new Image("file:movie" + i + ".jpg")); // 실제 영화 이미지 경로로 교체

            Label movieTitle = new Label("Movie Title " + i);
            movieTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            movieBox.getChildren().addAll(movieImage, movieTitle);

            // 각 movieBox에 클릭 이벤트 추가
            movieBox.setOnMouseClicked(event -> handleMovieSelection(movieBox, movieTitle.getText()));

            movieListVBox.getChildren().add(movieBox);
        }
    }

    // 영화 선택을 처리하는 메서드 (선택된 movieBox 강조 표시)
    private void handleMovieSelection(HBox movieBox, String movieTitle) {
        // 이미 선택된 영화가 있으면 강조 표시를 제거
        if (selectedMovieBox != null) {
            selectedMovieBox.setStyle(""); // 이전에 선택된 영화의 스타일을 초기화
        }

        // 새로운 movieBox를 선택하고 강조 스타일 적용
        selectedMovieBox = movieBox;
        selectedMovieTitle = movieTitle;  // 선택된 영화 제목을 저장
        selectedMovieBox.setStyle("-fx-border-color: blue; -fx-border-width: 3px; -fx-border-radius: 5px;");
    }

    // '영화 등록하기' 버튼 클릭 이벤트 처리 (MovieRegister.fxml)
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        if (selectedMovieTitle == null) {
            // 영화가 선택되지 않았을 경우 경고 메시지 출력
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("등록할 영화를 먼저 선택하세요.");
            alert.showAndWait();
            return;
        }

        try {
            // FXML 파일을 리소스 경로에서 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/MovieRegisterPopup.fxml"));
            Parent root = loader.load();

            // 팝업의 컨트롤러를 가져옴
            RegisterController popupController = loader.getController();

            // 선택한 영화의 제목을 팝업 창의 제목 필드에 설정
            popupController.setMovieTitle(selectedMovieTitle);

            // 팝업 컨트롤러에서 ComboBox 초기화
            popupController.resetComboBox();  // 팝업 창 내에서 ComboBox를 초기화

            Stage popupStage = new Stage();
            popupStage.setTitle("Movie Registration");
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(registerButton.getScene().getWindow());

            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            popupStage.show(); // 팝업을 띄움
        } catch (IOException e) {
            e.printStackTrace(); // 콘솔에 예외 메시지 출력
            // 예외 발생 시 사용자에게 경고창 띄우기
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("팝업창을 불러올 수 없습니다");
            alert.setContentText("영화 등록 창을 여는 도중 오류가 발생했습니다.");
            alert.showAndWait();
        }

    }

    public void resetComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList("2D", "3D", "4D", "4DX", "IMAX");
        kindComboBox.setItems(options);  // ComboBox에 선택 항목 추가
        kindComboBox.setValue("2D");  // 기본 선택값 설정
    }

    // 팝업창에서 영화 제목을 설정하는 메서드
    public void setMovieTitle(String movieTitle) {
        titleField.setText(movieTitle);
        titleField.setEditable(false);  // 제목을 읽기 전용으로 설정
    }

    
    // '등록 완료' 버튼 클릭 이벤트 처리 (MovieRegisterPopup.fxml)
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        String title = titleField.getText();
        String runtime = runtimeField.getText();
        String rating = ratingField.getText();
        String selectedKind = kindComboBox.getValue();

        // 입력된 영화 데이터를 출력하거나 저장 (여기서 DB에 저장할 수 있음)
        System.out.println("영화 제목: " + title);
        System.out.println("러닝타임: " + runtime);
        System.out.println("상영등급: " + rating);
        System.out.println("상영 종류: " + selectedKind);

        // 팝업 창을 닫기
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
    
    // 메뉴로 돌아가는 버튼 클릭 이벤트 처리
    @FXML
    private void handleBackToMenuAction(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/AdminMenuPopup.fxml"));
            Parent menuRoot = loader.load();

            // 현재 창의 Stage를 가져옴
            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            Scene scene = new Scene(menuRoot);

            // 메뉴 화면으로 전환
            stage.setScene(scene);
            stage.show();
            

        } catch (IOException e) {
            e.printStackTrace();
            // 예외 발생 시 사용자에게 경고창 표시
        }
    }
    
}
