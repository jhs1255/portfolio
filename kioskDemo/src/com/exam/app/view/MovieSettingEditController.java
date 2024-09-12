package com.exam.app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.DateCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import com.exam.app.AppData;

public class MovieSettingEditController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> theaterComboBox;
    @FXML
    private TextField timeTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Label movieTitleLabel;  // 영화 제목 표시용 Label
    
    private String selectedMovieTitle;  // 선택된 영화 제목
    private HashMap<String, ArrayList<String>> movieScreenings; // 영화 상영 정보 저장 구조

    @FXML
    public void initialize() {
        // 상영관의 메뉴 항목 설정
        theaterComboBox.getItems().addAll("1관", "2관", "3관");

        // 날짜 선택 시 오늘 날짜부터 일주일 이후까지만 선택할 수 있도록 설정
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        
        // 날짜 선택 시 오늘 이전의 날짜는 선택할 수 없도록 설정
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // 오늘부터 일주일 후까지만 선택 가능
                setDisable(empty || date.isBefore(today) || date.isAfter(oneWeekLater));
                if (date.isBefore(today)) {
                    setStyle("-fx-background-color: #ffc0cb;"); // 오늘 날짜 이전은 선택 불가
                }
            }
        });

        // 시간 입력 시 자동으로 'HH:mm' 형식으로 변환하는 리스너 추가
        timeTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 4 && newText.matches("\\d+")) {
                String formattedTime = newText.substring(0, 2) + ":" + newText.substring(2);
                timeTextField.setText(formattedTime);
            }
        });

        // 저장 버튼 클릭 시 상영 정보를 저장하고 화면을 전환하도록 설정
        saveButton.setOnAction(event -> saveScreeningInfo());
    }

    public void initializeData(String movieTitle, HashMap<String, ArrayList<String>> movieScreenings) {
        this.selectedMovieTitle = movieTitle;
        this.movieScreenings = movieScreenings;

        // 영화 제목을 Label에 표시합니다.
        movieTitleLabel.setText(movieTitle);

        // 현재 상영 정보가 있다면 해당 정보를 UI에 설정
        if (movieScreenings.containsKey(movieTitle)) {
            ArrayList<String> screenings = movieScreenings.get(movieTitle);
            if (!screenings.isEmpty()) {
                String lastScreening = screenings.get(screenings.size() - 1);  // 가장 최근 상영 정보
                String[] parts = lastScreening.split(", ");
                if (parts.length == 3) {
                    // 문자열을 LocalDate로 변환
                    LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE);
                    datePicker.setValue(date);  // 날짜
                    theaterComboBox.setValue(parts[1]);  // 상영관
                    timeTextField.setText(parts[2]);  // 시간
                }
            }
        }
    }

    // 상영 정보 저장 메서드
    private void saveScreeningInfo() {
        if (selectedMovieTitle != null) {
            LocalDate date = datePicker.getValue();
            String theater = theaterComboBox.getValue();
            String time = timeTextField.getText();
            
            if (date == null || theater == null || time == null || time.isEmpty()) {
                // 상영 정보가 제대로 입력되지 않았으면 저장하지 않고 리턴
                System.out.println("상영 정보가 완전하지 않습니다.");
                return;
            }

            // 영화에 해당하는 상영 정보 추가
            // 이미 있는 상영 정보에 추가하기
            // 상영 정보 추가
            String screeningInfo = String.format("%s, %s, %s", date, theater, time);
            HashMap<String, ArrayList<String>> screenings = AppData.getMovieScreenings();
            if (!screenings.containsKey(selectedMovieTitle)) {
                screenings.put(selectedMovieTitle, new ArrayList<>());
            }
            screenings.get(selectedMovieTitle).add(screeningInfo);
            
            System.out.println("상영 정보가 저장되었습니다: " + screeningInfo);
        }
        // 저장 후 SettingController로 돌아가기
        switchToMain();
    }

    // MovieSetting 화면으로 전환하는 메서드
    private void switchToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieSetting.fxml"));
            AnchorPane root = loader.load();

            Stage stage = (Stage) saveButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
