package com.exam.app.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.exam.app.AppData;

public class SettingController {

    @FXML
    private VBox movieListVBox;  // 영화 목록이 들어갈 VBox
    @FXML
    private Button registerButton;
    @FXML
    private Button backToMenuButton;

    private HBox selectedMovie;  // 선택된 영화 항목을 저장할 변수
    private String selectedMovieTitle;  // 선택된 영화 제목

    @FXML
    public void initialize() {
        // 영화 데이터를 AppData로부터 로드
        AppData.loadMovieScreenings();
        populateMovieList();
    }

    // 영화 목록을 동적으로 추가하는 메서드
    private void populateMovieList() {
        HashMap<String, ArrayList<HashMap<String, String>>> movieScreenings = AppData.getMovieScreenings();
        if (movieScreenings == null || movieScreenings.isEmpty()) {
            System.out.println("영화 목록이 없습니다.");
            return;
        }

        movieListVBox.getChildren().clear();  // 기존 영화 목록 클리어

        for (String movieTitle : movieScreenings.keySet()) {
            HBox movieItem = new HBox(10);  // 영화 항목은 HBox로 구성

            // 이미지뷰 생성 및 기본 이미지 설정 (경로는 실제 이미지 경로로 변경)
            ImageView movieImageView = new ImageView();
            movieImageView.setFitHeight(60);
            movieImageView.setFitWidth(50);

            // 영화 제목 및 정보 표시용 VBox 생성
            VBox movieDetailsVBox = new VBox(5);
            Label titleLabel = new Label(movieTitle);  // 영화 제목 레이블
            titleLabel.setId("movieTitleLabel");  // ID 추가
            
            // 상영 정보 레이블 생성 (AppData로부터 가져옴)
            String screeningType = "상영 종류: " + AppData.getMovieType(movieTitle);
            String rating = "관람가: " + AppData.getMovieRating(movieTitle);
            String duration = "상영 시간: " + AppData.getMovieRuntime(movieTitle);

            Label durationLabel = new Label(duration);
            Label ratingLabel = new Label(rating);
            Label screeningTypeLabel = new Label(screeningType);

            movieDetailsVBox.getChildren().addAll(titleLabel, screeningTypeLabel, durationLabel, ratingLabel);
            movieItem.getChildren().addAll(movieImageView, movieDetailsVBox);
            
            // 영화 항목에 클릭 이벤트 추가
            movieItem.setOnMouseClicked(event -> selectMovie(movieItem));

            movieListVBox.getChildren().add(movieItem);  // VBox에 추가
        }
    }

    // 영화 선택 시 파란색 테두리 추가 및 영화 제목 저장
    @FXML
    public void selectMovie(HBox movieItem) {
        if (selectedMovie != null) {
            selectedMovie.setStyle("");  // 이전 선택된 항목의 테두리 제거
        }

        selectedMovie = movieItem;
        selectedMovie.setStyle("-fx-border-color: blue; -fx-border-width: 2;");  // 파란색 테두리 추가

        Label movieTitleLabel = (Label) movieItem.lookup("#movieTitleLabel");
        selectedMovieTitle = movieTitleLabel.getText();  // 선택한 영화 제목 저장
    }

    // '상영 정보 추가/수정' 버튼 클릭 시 호출되는 메서드
    @FXML
    private void switchToEdit() {
        if (selectedMovieTitle == null) {
            // 영화가 선택되지 않았을 경우 처리
            System.out.println("영화를 선택하세요.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieSettingEdit.fxml"));
            Parent root = loader.load();

            MovieSettingEditController editController = loader.getController();
            // 선택된 영화 제목만 전달
            editController.initializeData(selectedMovieTitle);

            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // '상영 정보 확인' 버튼 클릭 시 호출되는 메서드
    @FXML
    public void switchToCheck(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieSettingCheck.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 메뉴로 돌아가는 버튼 클릭 이벤트 처리
    @FXML
    private void handleBackToMenuAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exam/app/view/AdminMenuPopup.fxml"));
            Parent menuRoot = loader.load();

            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            Scene scene = new Scene(menuRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
