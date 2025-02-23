package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.AppData;

public class MovieSettingEditController {
	@FXML private GridPane infoGridPane;

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> theaterComboBox;
    @FXML
    private TextField timeTextField, endtimeField;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML
    private Label movieTitleLabel1, movieTitleLabel2, screeningTypeLabel,ageRatingLabel,durationLabel;  // 영화 제목 표시용 Label
    
    @FXML private ImageView movieImageView;
    
    @FXML private HBox screeningInfoHBox;
    @FXML private VBox screeningInfoVBox;
    
    private String selectedMovieTitle;  // 선택된 영화 제목
    private String selectedMovieType; // 선택된 영화 유형
    private String selectedMovietype;
    private String selectedMovieRating;
    private String selectedMovieRuntime;
    private String selectedMoviePoster;
    private Integer selectedMovieId;
    private HashMap<String, ArrayList<String>> movieScreenings; // 영화 상영 정보 저장 구조
    
    private Connection con;
    private String url = "jdbc:mysql://localhost:3306/kiosk";

    public void initialize() {
    	 try {
             Class.forName("com.mysql.cj.jdbc.Driver");
             con = DriverManager.getConnection(url, "root", "root1234");
         } catch (Exception e) {
             e.printStackTrace();
         }
    	 
        // 상영관의 메뉴 항목 설정 - 아래에 loadTheaterComboBox()로 이동
        //theaterComboBox.getItems().addAll("1관", "2관", "3관", "특별관","IMAX관");

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
        // 시간 입력 시 해당 러닝타임에 맞춰 종료시간 입력
        timeTextField.textProperty().addListener((obs, oldText, newText) -> {
        	if (newText.length() == 4 && newText.matches("\\d+")) {
                String formattedTime = newText.substring(0, 2) + ":" + newText.substring(2);
                timeTextField.setText(formattedTime);
                updateEndTime();
            }
        });

        // 저장 버튼 클릭 시 상영 정보를 저장하고 화면을 전환하도록 설정
        saveButton.setOnAction(event -> saveScreeningInfo());
        // 뒤로 가기 버튼 클릭 시 이전 화면으로 돌아가도록 설정
        backButton.setOnAction(event -> switchToSettingController());
    }

    public void initializeData(Integer movie_id,String movietype,HashMap<String, ArrayList<String>> movieScreenings) {
        this.selectedMovieId = movie_id;
        this.selectedMovieType = movietype;
        this.movieScreenings = movieScreenings;

        
        try {
            String sql = "SELECT title, movietype, rating, runtime, poster FROM showmovie WHERE movie_id = ? and movietype=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, selectedMovieId);
            pstmt.setString(2, selectedMovieType);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                selectedMovieTitle = rs.getString("title");
                selectedMovietype = rs.getString("movietype");
                selectedMovieRating = rs.getString("rating");
                selectedMovieRuntime = rs.getString("runtime");
                selectedMoviePoster = rs.getString("poster");

                // 이미지 설정
                File file = new File(selectedMoviePoster);
                Image image = new Image(file.toURI().toString());
                movieImageView.setImage(image);
                movieImageView.setFitHeight(60);
                movieImageView.setFitWidth(100);

                // 라벨 설정
                movieTitleLabel1.setText(selectedMovieTitle);
                movieTitleLabel2.setText(selectedMovieTitle + "\t");
                screeningTypeLabel.setText(selectedMovietype);
                ageRatingLabel.setText(selectedMovieRating.equals("all") ? selectedMovieRating : selectedMovieRating + "세 이상관람가");
                durationLabel.setText(selectedMovieRuntime + "분");
                
                //상영관 정보 로드
                loadTheaterComboBox();
                //화면 하단 상자 정보
                loadScreenInfo();
                // 상영 정보 로드
                movieScreenings = AppData.getMovieScreenings();
                if (movieScreenings.containsKey(selectedMovieTitle)) {
                    ArrayList<String> screenings = movieScreenings.get(selectedMovieTitle);
                    if (!screenings.isEmpty()) {
                        String lastScreening = screenings.get(screenings.size() - 1);
                        String[] parts = lastScreening.split(", ");
                        if (parts.length == 3) {
                            LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE);
                            datePicker.setValue(date);
                            theaterComboBox.setValue(parts[1]);
                            timeTextField.setText(parts[2]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 상영관 종류에 따른 상영관 선택
    public void loadTheaterComboBox() {
    	try {
   		 String sql = "SELECT section FROM theater WHERE kind = ?";
   		 PreparedStatement pstmt = con.prepareStatement(sql);
   		 pstmt.setString(1, selectedMovietype); // 영화의 movietype을 기준으로 상영관 종류를 조회
            ResultSet rs = pstmt.executeQuery();
            
            // 기존 ComboBox 항목 제거
            theaterComboBox.getItems().clear();

	         while (rs.next()) {
	             String section = rs.getString("section");
	             theaterComboBox.getItems().add(section);
	         }
	   	 }catch(Exception e) {
	   		 e.printStackTrace();
	   	 }
    }
    
    //화면 하단 상자 정보
    public void loadScreenInfo() {
    	screeningInfoHBox.getChildren().clear(); // 기존 항목 제거
    	
    	try {
    		//상영날짜 상영시작시간 가져옴
    		String sql = "SELECT pi.theater_id, pi.movie_date, pi.start_time, pi.end_time,t.kind " +
                    "FROM play_info pi " +
                    "JOIN theater t ON pi.theater_id = t.theater_id " +
                    "WHERE pi.movie_id = ? AND t.kind = ?";
    		PreparedStatement pstmt = con.prepareStatement(sql);// sql 연결
    		pstmt.setInt(1, selectedMovieId); // 일치하는 movie_id찾기위해 가져옴
    		 pstmt.setString(2, selectedMovieType);// 일치하는 영화관 종류를 찾기위헤 movietype값을 가져옴
    		ResultSet rs = pstmt.executeQuery();
    		
    		while(rs.next()) {
    			int theaterId = rs.getInt("theater_id");
                LocalDate movieDate = rs.getDate("movie_date").toLocalDate();
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                
                // theater_id를 기반으로 상영관 정보 가져오기
                String theaterSql = "SELECT section,kind FROM theater WHERE theater_id = ?";
                PreparedStatement theaterPstmt = con.prepareStatement(theaterSql);
                theaterPstmt.setInt(1, theaterId);
                ResultSet theaterRs = theaterPstmt.executeQuery();
                //상영관
                String section = "정보 없음";
                //상영관 종류
                String kind = "정보없음";
                if (theaterRs.next()) {
                    section = theaterRs.getString("section");
                    kind = theaterRs.getString("kind");
                }
                
                // 날짜, 시작 시간
                String date = movieDate.toString();
                String stime = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                String etime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                // 새로운 상영 정보를 담은 VBox 생성
                VBox vbox = new VBox(5);
                vbox.setPadding(new Insets(5, 10, 5, 10));
                vbox.setAlignment(Pos.CENTER);

                // StackPane을 사용하여 VBox의 중앙에 삭제 버튼을 겹쳐 배치
                StackPane stackPane = new StackPane();
                // 테두리 설정
                stackPane.setBorder(new Border(
                    new BorderStroke(
                        Color.BLACK, 
                        BorderStrokeStyle.SOLID, 
                        null, 
                        new BorderWidths(0.5)
                    )
                ));

                Label dateLabel = new Label(date);
                Label theaterLabel = new Label(section);
                Label timeLabel = new Label(stime);
                Label endtimeLabel = new Label("~"+etime);
                Label kindLabel = new Label(kind);

                vbox.getChildren().addAll(dateLabel, kindLabel,theaterLabel, timeLabel,endtimeLabel);

                // 삭제 버튼 추가
                Button deleteButton = new Button("삭제");
                deleteButton.setVisible(false);
                deleteButton.setOnAction(event -> deleteScreeningInfo(stackPane));

                // StackPane에 VBox와 삭제 버튼 추가
                stackPane.getChildren().addAll(vbox, deleteButton);
                stackPane.setAlignment(deleteButton, Pos.CENTER);

                // VBox 클릭 시 삭제 버튼 표시/숨기기
                stackPane.setOnMouseClicked(event -> toggleDeleteButton(deleteButton));

                // VBox를 HBox에 추가
                screeningInfoHBox.getChildren().add(stackPane);

                // 입력 필드 초기화
                datePicker.setValue(null);
                theaterComboBox.setValue(null);
                timeTextField.clear();
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}//try
    }// loadScreenInfo
    
    //상영 시작 시간을 가져와 종료시간 계산하기
    private void updateEndTime() {
    	String runtime = selectedMovieRuntime;
    	String startTime = timeTextField.getText();
    	
    	if (startTime != null && !startTime.isEmpty() && runtime != null && !runtime.isEmpty()) {
            try {
                // 상영 시작 시간 파싱
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime starttime = LocalTime.parse(startTime, formatter);

                // 런타임 파싱
                int runtimeMinutes = Integer.parseInt(runtime);

                // 종료 시간 계산
                LocalTime endTime = starttime.plus(runtimeMinutes, ChronoUnit.MINUTES);

                // 종료 시간 표시
                endtimeField.setText(endTime.format(formatter));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                endtimeField.setText("");
            }
        } else {
            endtimeField.setText("");
        }
    }//상영종료시간 계산

    // 상영 정보 저장 메서드
    @FXML
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
    	 }
    	//입력한 날짜 가져오기
    	LocalDate localDate = datePicker.getValue();
    	//입력한 상영 시작 시간 가져오기
    	LocalTime localStartTime = LocalTime.parse(timeTextField.getText());
    	//계산된 상영 종료 시간 가져오기
    	LocalTime localEndTime = LocalTime.parse(endtimeField.getText());
    	
    	//sql에 넣기 위한 날짜 및 시간변환
    	Date sqlDate = Date.valueOf(localDate);
    	Time sqlStartTime = Time.valueOf(localStartTime);
    	Time sqlEndTime = Time.valueOf(localEndTime);
    	
    	//선택한 상영관 가져오기
    	String theater = theaterComboBox.getValue();
    	
    	try {
    		int theater_id = getTheaterId(theater);//화면 하단에 theater_id를 받아오는 함수 있음
    		// 기존 상영 정보와 종료 시간이 충돌하지 않는지 확인
            if (!isEndTimeValid(localDate, localStartTime, localEndTime, theater_id)) {
                System.out.println("상영 종료 시간이 기존 상영 시간과 충돌합니다.");
                return;
            }
    		String sql = "INSERT INTO play_info(movie_id,theater_id,movie_date,start_time,end_time) VALUES(?,?,?,?,?)";
    		PreparedStatement pstmt = con.prepareStatement(sql);
    		pstmt.setInt(1, selectedMovieId);
    		pstmt.setInt(2, theater_id);
    		pstmt.setDate(3, sqlDate);
    		pstmt.setTime(4, sqlStartTime);
    		pstmt.setTime(5, sqlEndTime);
    		pstmt.executeUpdate();
    		System.out.println("상영 정보가 성공적으로 저장되었습니다.");
    		
    		loadScreenInfo();
    	}catch(SQLException e) {
    		System.err.println("상영 정보 저장 중 오류 발생:");
            e.printStackTrace();
    	}

        // 입력 필드 초기화
        datePicker.setValue(null);
        theaterComboBox.setValue(null);
        timeTextField.clear();
    }
    
    //선택한 상영관과 일치하는 theater_id값 가져오기
    private int getTheaterId(String theaterName) throws SQLException {
    	int theaterId = 1;
		String sql = "SELECT theater_id FROM theater WHERE section = ?";
	    PreparedStatement pstmt = con.prepareStatement(sql);
	    pstmt.setString(1, theaterName);
	    ResultSet rs = pstmt.executeQuery();
	    if (rs.next()) {
	        theaterId = rs.getInt("theater_id");
	    }
	    return theaterId;
    }
    
    //상영 시간이 겹치는지 조회
    private boolean isEndTimeValid(LocalDate date, LocalTime startTime, LocalTime endTime, int theaterId) throws SQLException {
        String sql = "SELECT start_time, end_time FROM play_info WHERE movie_date = ? AND theater_id = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setDate(1, Date.valueOf(date));
        pstmt.setInt(2, theaterId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            LocalTime existingStartTime = rs.getTime("start_time").toLocalTime();
            LocalTime existingEndTime = rs.getTime("end_time").toLocalTime();

            if (startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
                // 새로운 상영 시간이 기존 상영 시간과 겹침
                return false;
            }
        }
        return true;
    }
    
    //화면에서 StackPane지우기
    private void deleteScreeningInfo(StackPane stackPane) {
    	 // StackPane에서 영화 상영 정보를 얻기 위한 VBox 접근
        VBox vbox = (VBox) stackPane.getChildren().get(0);
        Label dateLabel = (Label) vbox.getChildren().get(0);
        Label timeLabel = (Label) vbox.getChildren().get(3);

        // 상영 정보 추출
        String date = dateLabel.getText();
        String startTime = timeLabel.getText();
        System.out.println("받은날짜: "+date);
        System.out.println("받은시간: "+startTime);
        
        try {
        	// Time 객체에 시:분:초 형식 필요
            String formattedStartTime = startTime + ":00"; // 'HH:MM:SS' 형식으로 변환
        	// 상영 정보 삭제를 위한 SQL 쿼리
            String sql = "DELETE FROM play_info WHERE movie_id = ? AND movie_date = ? AND start_time = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, selectedMovieId);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setTime(3, Time.valueOf(formattedStartTime)); // Time 객체에 시:분:초 형식 필요
            pstmt.executeUpdate();

            System.out.println("상영 정보가 성공적으로 삭제되었습니다.");
        	
        	screeningInfoHBox.getChildren().remove(stackPane);
            // VBox를 삭제한 후 남아있는 VBox들을 왼쪽으로 이동시키기
            for (int i = 0; i < screeningInfoHBox.getChildren().size(); i++) {
                StackPane currentStackPane = (StackPane) screeningInfoHBox.getChildren().get(i);
                VBox currentVBox = (VBox) currentStackPane.getChildren().get(0);
                if (i == 0) {
                    // 첫 번째 VBox에만 padding 설정
                    currentVBox.setPadding(new Insets(0, 0, 0, 0));
                } else {
                    currentVBox.setPadding(new Insets(0, 0, 0, 10)); // 나머지 VBox는 간격을 줍니다.
                }
            }
        }catch(Exception e) {
        	System.err.println("상영 정보 삭제 중 오류 발생:");
        	e.printStackTrace();
        }//try-catch()
              
    }

    private void toggleDeleteButton(Button deleteButton) {
        if (deleteButton.isVisible()) {
            deleteButton.setVisible(false);
        } else {
            deleteButton.setVisible(true);
        }
    }

    // MovieSetting 화면으로 전환하는 메서드
    private void switchToSettingController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MovieSetting.fxml"));
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
