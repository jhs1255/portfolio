package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import dto.Movie;
import dto.MovieData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EasyDate implements Initializable {
    @FXML private StackPane EasyDateStackPane;
	@FXML private Button help;
    @FXML private Text EasyDateToday; // 오늘 날짜 설정
    @FXML private Button EasyDateHomeBtn; // 홈버튼
    @FXML private Button EasyDateNextBtn; // 다음페이지 버튼
    @FXML private Button EasyDateBackBtn; // 돌아가기 버튼
    @FXML private Button EasyDateBtn1, EasyDateBtn2, EasyDateBtn3, EasyDateBtn4,
            EasyDateBtn5, EasyDateBtn6, EasyDateBtn7; // 날짜 선택버튼

    private DateTimeFormatter formatter, buttonformatter;
    private LocalDate selectedDate; // Store the selected date
    private Button[] buttons;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        LocalDate today = LocalDate.now();
        formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)");
        buttonformatter = DateTimeFormatter.ofPattern("d일\n(E)");
        EasyDateToday.setText(today.format(formatter)); // 오늘 날짜 설정

        EasyDateBtn1.getStyleClass().add("selected");

        // 버튼 배열 초기화
        buttons = new Button[]{EasyDateBtn1, EasyDateBtn2, EasyDateBtn3, EasyDateBtn4,
                EasyDateBtn5, EasyDateBtn6, EasyDateBtn7};

        // 오늘 날짜 기준 7일 버튼에 날짜 설정
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(today.plusDays(i).format(buttonformatter));
        }

        // 모든 버튼에 클릭 이벤트 핸들러 추가
        for (Button button : buttons) {
            button.setOnMouseClicked(event -> handleButtonClick(button));
            button.setOnAction(event -> handleDateSelection(event));
        }

        EasyDateBackBtn.setOnAction(event -> switchBackPage(event)); // 이전화면으로 이동
        EasyDateHomeBtn.setOnAction(event -> switchHome(event));     // 홈화면으로 이동
        EasyDateNextBtn.setOnAction(event -> switchNextPage(event)); // 다음페이지로 이동
    }

    //버튼 클릭 시 선택한 버튼 색상 변경 및 제거
    private void handleButtonClick(Button clickedButton) {
        for (Button button : buttons) {
            button.getStyleClass().remove("selected");
        }
        clickedButton.getStyleClass().add("selected");
    }

    @FXML
    public void handleDateSelection(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        LocalDate today = LocalDate.now();

        // 버튼에 설정된 날짜를 가져오기 위해 버튼의 ID에 따라 날짜를 계산
        switch (selectedButton.getId()) {
            case "EasyDateBtn1":
                selectedDate = today;
                break;
            case "EasyDateBtn2":
                selectedDate = today.plusDays(1);
                break;
            case "EasyDateBtn3":
                selectedDate = today.plusDays(2);
                break;
            case "EasyDateBtn4":
                selectedDate = today.plusDays(3);
                break;
            case "EasyDateBtn5":
                selectedDate = today.plusDays(4);
                break;
            case "EasyDateBtn6":
                selectedDate = today.plusDays(5);
                break;
            case "EasyDateBtn7":
                selectedDate = today.plusDays(6);
                break;
            default:
                break;
        }

        if (selectedDate != null) {
            String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd(E)"));
            System.out.println("선택날짜: " + formattedDate);
        }
    }

    // 홈으로 이동
    private void switchHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyMonitor.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) EasyDateHomeBtn.getScene().getWindow();
            Scene scene = new Scene(homeRoot); // 홈화면으로 전환
            stage.setScene(scene);
            stage.show(); // 화면에 출력
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 인원 선택 창으로 이동
    public void switchNextPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyChoiceMovie.fxml"));
            Parent nextPageRoot = loader.load();

            // 선택한 날짜를 EasyChoiceMovie 컨트롤러에 전달
            EasyChoiceMovie controller = loader.getController();
            if (selectedDate != null) {
                String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd(E)"));
                controller.loadMovieList(formattedDate); // 선택된 날짜 전달
            }

            Stage stage = (Stage) EasyDateNextBtn.getScene().getWindow();
            Scene scene = new Scene(nextPageRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchBackPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyMonitor.fxml"));
            Parent backPageRoot = loader.load();
            Stage stage = (Stage) EasyDateBackBtn.getScene().getWindow();
            Scene scene = new Scene(backPageRoot); // 이전화면으로 전환
            stage.setScene(scene);
            stage.show(); // 화면에 출력
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
