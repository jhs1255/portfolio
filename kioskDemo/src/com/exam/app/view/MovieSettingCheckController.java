package com.exam.app.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.exam.app.AppData;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MovieSettingCheckController {

    @FXML
    private VBox screeningInfoVBox;

    @FXML
    private Button okButton;

    @FXML
    public void initialize() {
        // OK 버튼 클릭 시 메인 화면으로 전환
        okButton.setOnAction(event -> switchToMain());
    }

    // 상영 정보 데이터를 세팅하는 메서드
    public void setMovieScreenings() {
        displayScreeningInfo();
    }

    private void displayScreeningInfo() {
        // 이전에 추가된 정보를 초기화
        screeningInfoVBox.getChildren().clear();

        // 각 영화와 상영 정보를 출력
        HashMap<String, ArrayList<String>> movieScreenings = AppData.getMovieScreenings();

        for (String movieTitle : movieScreenings.keySet()) {
            ArrayList<String> screenings = movieScreenings.get(movieTitle);

            for (String screening : screenings) {
                HBox screeningBox = new HBox();
                screeningBox.setSpacing(20);

                ImageView posterImageView = new ImageView();
                posterImageView.setFitWidth(100);
                posterImageView.setFitHeight(150);

                Label titleLabel = new Label("영화 제목: " + movieTitle);
                String[] screeningParts = screening.split(", ");
                Label dateLabel = new Label("상영 날짜: " + screeningParts[0]);
                Label theaterLabel = new Label("상영관: " + screeningParts[1]);
                Label timeLabel = new Label("상영 시간: " + screeningParts[2]);

                VBox infoBox = new VBox();
                infoBox.getChildren().addAll(titleLabel, dateLabel, theaterLabel, timeLabel);

                screeningBox.getChildren().addAll(posterImageView, infoBox);

                screeningInfoVBox.getChildren().add(screeningBox);
            }
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
