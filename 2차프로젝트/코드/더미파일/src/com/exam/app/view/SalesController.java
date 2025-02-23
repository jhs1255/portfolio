package com.exam.app.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class SalesController {

    @FXML
    private GridPane calendarGrid;
    @FXML
    private GridPane yearGrid;
    @FXML
    private Label calendarLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Button prevMonthBtn;
    @FXML
    private Button nextMonthBtn;
    @FXML
    private Button prevYearBtn;
    @FXML
    private Button nextYearBtn;
    @FXML
    private TabPane tabPane;

    private LocalDate currentDate;
    private YearMonth currentYearMonth;
    
    @FXML
    private Button backToMenuButton;

    @FXML
    public void initialize() {
        currentDate = LocalDate.now();
        currentYearMonth = YearMonth.now();

        setupCalendar();
        setupYearlySales();

        prevMonthBtn.setOnAction(e -> changeMonth(-1));
        nextMonthBtn.setOnAction(e -> changeMonth(1));
        prevYearBtn.setOnAction(e -> changeYear(-1));
        nextYearBtn.setOnAction(e -> changeYear(1));

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab.getText().equals("일별")) {
                currentYearMonth = YearMonth.from(currentDate);
                setupCalendar();
            } else if (newTab.getText().equals("월별")) {
                setupYearlySales();
            }
        });
        
        calendarGrid.setMinSize(calendarGrid.getPrefWidth(), calendarGrid.getPrefHeight());
        calendarGrid.layout(); // 레이아웃을 강제로 업데이트
    }

    private void setupCalendar() {
        calendarGrid.getChildren().clear();
        calendarGrid.getRowConstraints().clear(); // 기존 RowConstraints 제거

        int year = currentYearMonth.getYear();
        int month = currentYearMonth.getMonthValue();
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int daysInMonth = firstDayOfMonth.lengthOfMonth();
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        int column = (firstDayOfWeek - 1) % 7;
        int row = 0;

        // 행 높이 설정 (여기서는 모든 행의 높이를 50으로 설정, 필요에 따라 조정)
        double rowHeight = 50;

        // 최대 6주까지 처리
        int numberOfWeeks = (int) Math.ceil((daysInMonth + firstDayOfWeek - 1) / 7.0);
        for (int i = 0; i < numberOfWeeks; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(rowHeight);
            rowConstraints.setPrefHeight(rowHeight);
            rowConstraints.setMaxHeight(rowHeight);
            calendarGrid.getRowConstraints().add(rowConstraints);
        }

        // 일별 매출을 저장할 Map
        Map<Integer, Double> dailySales = new HashMap<>();

        for (int i = 1; i <= daysInMonth; i++) {
            final int day = i;
            double sales = getSalesForDay(day);
            dailySales.put(day, sales);

            // 매출을 천원 단위로 반올림
            double roundedSales = Math.round(sales / 1000.0) * 1000;

            Label dayLabel = new Label(String.valueOf(day));
            Label salesLabel = new Label(String.format("%,d", (int)roundedSales));

            dayLabel.setFont(new Font(14));
            salesLabel.setFont(new Font(14));

            if (calendarGrid.getWidth() < 300) {
                dayLabel.setFont(new Font(10));
                salesLabel.setFont(new Font(10));
            }

            dayLabel.setOnMouseClicked(e -> System.out.println("Clicked on day " + day));

            calendarGrid.add(dayLabel, column, row);
            calendarGrid.add(salesLabel, column, row + 1);

            column++;
            if (column == 7) {
                column = 0;
                row += 2;
            }
        }

        calendarLabel.setText(currentYearMonth.getYear() + "/" + String.format("%02d", currentYearMonth.getMonthValue()));
    }

    private void setupYearlySales() {
        yearGrid.getChildren().clear();
        int currentYear = currentYearMonth.getYear();
        yearLabel.setText(String.valueOf(currentYear));

        // 12개월의 매출을 저장할 Map
        Map<Integer, Double> monthlySales = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);

            // 월별 매출을 계산하기 위한 변수
            double totalMonthlySales = 0;

            // 해당 월의 모든 일별 매출 합산
            for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
                totalMonthlySales += getSalesForDay(day);
            }

            // 매출을 천원 단위로 반올림
            final double roundedMonthlySales = Math.round(totalMonthlySales / 1000.0) * 1000;

            monthlySales.put(month, roundedMonthlySales);

            final int capturedMonth = month;
            Label monthLabel = new Label();
            monthLabel.setText(yearMonth.getMonth().name() + "\n" + String.format("%,d", (int)roundedMonthlySales));
            monthLabel.setOnMouseClicked(e -> {
                System.out.println("Clicked on month " + capturedMonth + ": ₩" + String.format("%,d", (int)roundedMonthlySales));
            });

            monthLabel.setFont(new Font(14));

            if (yearGrid.getWidth() < 300) {
                monthLabel.setFont(new Font(10));
            }

            yearGrid.add(monthLabel, (month - 1) % 3, (month - 1) / 3);
        }
    }

    private void changeMonth(int delta) {
        currentYearMonth = currentYearMonth.plusMonths(delta);
        setupCalendar();
    }

    private void changeYear(int delta) {
        currentYearMonth = currentYearMonth.plusYears(delta);
        setupYearlySales();
    }

    private double getSalesForDay(int day) {
        return Math.random() * 100000;  // 천원 단위로 매출 데이터 생성
    }

    private double getSalesForMonth(YearMonth yearMonth) {
        double totalSales = 0;
        int daysInMonth = yearMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            totalSales += getSalesForDay(day);
        }
        return totalSales;
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
