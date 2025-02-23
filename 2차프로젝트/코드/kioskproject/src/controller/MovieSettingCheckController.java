package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dto.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MovieSettingCheckController {
	private Connection con;
//    private String url = "jdbc:mysql://localhost:3306/kiosk";
	

    @FXML
    private VBox screeningInfoVBox;

    @FXML
    private Button okButton;

    @FXML
    public void initialize() {
        // OK 버튼 클릭 시 메인 화면으로 전환
        okButton.setOnAction(event -> switchToMain());
        con = DBConnection.getConnection();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection(url, "root", "root1234");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        setMovieScreenings();
    }

    // 상영 정보 데이터를 세팅하는 메서드
    public void setMovieScreenings() {
        displayScreeningInfo();
    }

    private void displayScreeningInfo() {
        // 이전에 추가된 정보를 초기화
        screeningInfoVBox.getChildren().clear();
        
        //Play_info 테이블에서 상영정보를 가져와서 추가
        try {
        	String sql = "SELECT sm.title, sm.runtime, sm.rating, sm.poster, sm.movietype, t.kind, t.section,t.seat, p.movie_date, p.start_time, p.end_time "+
        				 "FROM play_info p "+
        				 "JOIN showmovie sm ON p.movie_id = sm.movie_id "+
        				 "JOIN theater t ON p.theater_id = t.theater_id "+
        				 "where sm.movietype = t.kind " +
        				 "ORDER BY sm.title, t.kind, p.movie_date";
        	PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            // 영화 정보를 저장할 맵
            Map<String, MovieInfo> movieMap = new HashMap<>();
            while(rs.next()) {
            	String movieTitle = rs.getString("title");
            	String runtime = rs.getString("runtime");
            	String rating = rs.getString("rating");
            	String poster = rs.getString("poster");
            	String movieType = rs.getString("kind");
            	Integer seat = rs.getInt("seat");
            	String movieSection = rs.getString("section");
            	LocalDate moviedate = rs.getDate("movie_date").toLocalDate();
            	LocalTime startTime = rs.getTime("start_time").toLocalTime();
            	LocalTime endTime = rs.getTime("end_time").toLocalTime();
            	
            	String date = moviedate.toString();
            	String stime = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            	String etime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            	
            	String movieKey = movieTitle + "|" + rating + "|" + movieType + "|" +runtime +"|" + poster;

                // 영화 정보를 맵에 추가
                if (!movieMap.containsKey(movieKey)) {
                    movieMap.put(movieKey, new MovieInfo(movieTitle, rating, runtime, poster, movieType));
                }

                // 상영 정보 추가
                movieMap.get(movieKey).addScreening(date, stime, etime, seat, movieSection);     	
               
            }//while
            
         // UI에 영화 정보와 상영 정보 출력
            for (MovieInfo movie : movieMap.values()) {
                HBox screeningBox = new HBox();
                screeningBox.setSpacing(20);
                
                Image image;
	            if (movie.getPosterUrl().startsWith("http://") || movie.getPosterUrl().startsWith("https://")) {
	                // URL인 경우
	                image = new Image(movie.getPosterUrl()); // URL로부터 이미지 로드
	            } else {
	                // 로컬 파일인 경우
	                File file = new File(movie.getPosterUrl());
	                image = new Image(file.toURI().toString());
	            }
	            ImageView imageview = new ImageView(image);
	            imageview.setFitHeight(70);
	            imageview.setFitWidth(100);

//                ImageView posterImageView = new ImageView();
//                posterImageView.setFitWidth(100);
//                posterImageView.setFitHeight(130);
//
//                // 포스터 이미지 설정
//                File file = new File(movie.getPosterUrl());
//                Image image = new Image(file.toURI().toString());
//                posterImageView.setImage(image);

                Label titleLabel = new Label(movie.getTitle() + " " + movie.getMovieType());
                Label ratingLabel = new Label(movie.getRating().equals("all") ? movie.getRating() : movie.getRating() + "세 이상관람가");
                Label runtimeLabel = new Label(movie.getRuntime() + "분");

                VBox infoBox = new VBox();
                VBox timeBox = new VBox();
                timeBox.setSpacing(5);
                infoBox.getChildren().addAll(titleLabel, ratingLabel, runtimeLabel);
               
                for (Screening screening : movie.getScreenings()) {
                	//추후 css작업 시 가로 배치하기 위하여 생성
                	/*HBox screeningHBox = new HBox();
                    screeningHBox.setSpacing(10);*/
                	VBox timeDetailVBox = new VBox();
                    Label dateLabel = new Label("상영 날짜: " + screening.getDate());
                    Label timeLabel = new Label("상영 시간: " + screening.getStartTime() + " - " + screening.getEndTime());
                    Label sectionLabel = new Label("상영관: " + screening.getSection() +" "+ screening.getSeat()+"석");
                    timeDetailVBox.getChildren().addAll(dateLabel, timeLabel, sectionLabel);
                    timeDetailVBox.setStyle("-fx-border-color: black;");
                    //추후 가로배치 시 사용
                    /*screeningHBox.getChildren().add(timeDetailVBox);
                    timeBox.getChildren().add(screeningHBox);*/
                    timeBox.getChildren().add(timeDetailVBox);                  
                }
                infoBox.getChildren().add(timeBox);
                screeningBox.getChildren().addAll(imageview, infoBox);
                screeningBox.setStyle("-fx-border-color:blue;");
                screeningInfoVBox.getChildren().add(screeningBox);
            }
            
        }catch(Exception e) {
        	e.printStackTrace();
        }

    }
    
    //상영정보 설정화면
    private void switchToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MovieSetting.fxml"));
            AnchorPane root = loader.load(); // AnchorPane으로 수정

            Stage stage = (Stage) okButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static class MovieInfo {
        private final String title;
        private final String rating;
        private final String runtime;
        private final String poster;
        private final String movieType;
        private final ArrayList<Screening> screenings;

        public MovieInfo(String title, String rating, String runtime, String poster, String movieType) {
            this.title = title;
            this.rating = rating;
            this.runtime = runtime;
            this.poster = poster;
            this.movieType = movieType;
            this.screenings = new ArrayList<>();
        }

        public void addScreening(String date, String startTime, String endTime,Integer seat, String section) {
            screenings.add(new Screening(date, startTime, endTime, seat, section));
        }

        public String getTitle() {
            return title;
        }

        public String getRating() {
            return rating;
        }

        public String getRuntime() {
            return runtime;
        }

        public String getPosterUrl() {
            return poster;
        }

        public String getMovieType() {
            return movieType;
        }

        public ArrayList<Screening> getScreenings() {
            return screenings;
        }
    }

    private static class Screening {
        private final String date;
        private final String startTime;
        private final String endTime;
        private final String section;
		private final Integer seat;

        public Screening(String date, String startTime, String endTime, Integer seat, String section) {
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.seat = seat;
            this.section = section;
        }

        public String getDate() {
            return date;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getSection() {
            return section;
        }
        
        public Integer getSeat() {
        	return seat;
        }
    }
}
