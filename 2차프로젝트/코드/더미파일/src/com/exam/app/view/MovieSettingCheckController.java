package com.exam.app.view;

import com.exam.app.AppData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieSettingCheckController {

    @FXML
    private VBox screeningInfoVBox;  // 전체 상영 정보를 담을 VBox
    @FXML
    private Button okButton;

    @FXML
    public void initialize() {
    	// 확인 버튼 클릭 시 메인 화면으로 전환
        okButton.setOnAction(event -> switchToMain());
        
        // AppData에서 영화 상영 정보를 불러옵니다.
        AppData.loadMovieScreenings();  // 테스트 데이터를 로드

        // 영화 상영 정보를 화면에 동적으로 추가
        HashMap<String, ArrayList<HashMap<String, String>>> movieScreenings = AppData.getMovieScreenings();

        // 영화마다 정보를 추가
        for (String movieTitle : movieScreenings.keySet()) {
            // 영화 정보를 표시하는 HBox 생성
            HBox movieInfoBox = new HBox();
            movieInfoBox.setPrefWidth(320);
            movieInfoBox.setPrefHeight(100);

            // 영화 포스터 (테스트에서는 기본 이미지 사용)
            ImageView posterImageView = new ImageView();
            posterImageView.setFitHeight(70);
            posterImageView.setFitWidth(70);
            // posterImageView에 실제 포스터 이미지 경로를 설정할 수 있음

            // 영화 제목, 상영 종류, 관람가, 러닝타임 정보를 담는 VBox
            VBox movieDetailsBox = new VBox();
            movieDetailsBox.setPrefWidth(230);

            Label titleLabel = new Label(movieTitle);
            Label typeLabel = new Label("(" + AppData.getMovieType(movieTitle) + ")");
            Label ratingLabel = new Label(AppData.getMovieRating(movieTitle));
            Label runtimeLabel = new Label(AppData.getMovieRuntime(movieTitle));

            movieDetailsBox.getChildren().addAll(titleLabel, typeLabel, ratingLabel, runtimeLabel);

            // HBox에 포스터와 영화 정보를 추가
            movieInfoBox.getChildren().addAll(posterImageView, movieDetailsBox);

            // movieInfoBox를 screeningInfoVBox에 추가
            screeningInfoVBox.getChildren().add(movieInfoBox);

            // 새로운 HBox 생성: 이 HBox는 각 영화의 상영 정보를 담을 컨테이너 역할
            HBox screeningInfoHBox = new HBox();
            screeningInfoHBox.setPrefWidth(320);
            screeningInfoHBox.setPrefHeight(60);
            screeningInfoHBox.setSpacing(10);  // 각 상영 정보 사이의 간격을 조정
            

            // 각 영화의 상영 정보를 반복하여 추가
            ArrayList<HashMap<String, String>> screenings = movieScreenings.get(movieTitle);
            for (HashMap<String, String> screeningInfoData : screenings) {
                // 상영 정보를 담는 새로운 VBox 생성
                VBox screeningDetailsBox = new VBox();
                screeningDetailsBox.setPrefWidth(60);
                screeningDetailsBox.setSpacing(5);

                // 상영 날짜, 상영관, 상영 시간을 추가
                Label dateLabel = new Label(screeningInfoData.get("date"));
                Label theaterLabel = new Label(screeningInfoData.get("theater"));
                Label timeLabel = new Label(screeningInfoData.get("time"));

                screeningDetailsBox.getChildren().addAll(dateLabel, theaterLabel, timeLabel);

                // VBox 정렬 설정
                screeningDetailsBox.setAlignment(Pos.CENTER);  // 중앙 정렬

                // 새로 생성한 screeningDetailsBox를 screeningInfoHBox에 추가
                screeningInfoHBox.getChildren().add(screeningDetailsBox);
                VBox.setMargin(screeningInfoHBox, new Insets(0,0,10,0));
            }

            // 영화별로 상영 정보 HBox를 추가
            screeningInfoVBox.getChildren().add(screeningInfoHBox);
        }
    }
    private void switchToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieSetting.fxml"));
            AnchorPane root = loader.load(); // AnchorPane으로 수정

            Stage stage = (Stage) okButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
