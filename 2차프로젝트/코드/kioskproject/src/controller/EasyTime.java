package controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import dto.DBConnection;
import dto.Movie;
import dto.MovieData;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.stage.Stage;
import javafx.util.Duration;

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
import java.sql.Connection;
import java.sql.DriverManager;



public class EasyTime implements Initializable{
    @FXML private StackPane EasyTimePane;
    @FXML private Button home; // 홈버튼
    @FXML private Button nextPage; // 다음으로 버튼 
    @FXML private Button backBtn; // 이전으로 버튼
    @FXML private VBox movieList; // 영화 목록 패널
    @FXML private Label MovieInfo; // 영화 정보
    MovieData movieData;
//    String url = "jdbc:mysql://localhost:3306/kiosk";
	private Connection con;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        movieData = new MovieData();
        con = DBConnection.getConnection();
//        try {
//    		Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("데이터베이스 연결중...");
//			con = DriverManager.getConnection(url,"root","db2023");
//			System.out.println("연결성공");
//
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    	}

        home.setOnAction(event->BackHome(event));// home 버튼
        backBtn.setOnAction(event->BackPage(event)); //이전으로 버튼
        nextPage.setOnAction(event->NextPage(event)); //다음으로 버튼       

    }
    //영화 목록 출력 (날짜와 영화 정보를 받아와 해당 날짜와 선택한 영화의 시간의 버튼 생성)
	void loadMovieList(String selectedDate, String movieTitle, String rating, String kind) {
        // 이전에 추가된 정보를 초기화
        movieList.getChildren().clear();
        System.out.println(selectedDate);
        System.out.println("영화 목록을 출력");
    
        try {
            // SQL 쿼리에 조건 추가
            String sql = "SELECT sm.title, sm.runtime, sm.rating, sm.poster, sm.movietype, t.kind, t.section, t.seat, p.play_info_id, p.movie_date, p.start_time, p.end_time " +
                         "FROM play_info p " +
                         "JOIN showmovie sm ON p.movie_id = sm.movie_id " +
                         "JOIN theater t ON p.theater_id = t.theater_id " +
                         "WHERE sm.movietype = t.kind " +
                         "AND p.movie_date = ? " +
                         "AND sm.title = ? " +
                         "AND sm.rating = ? " +
                         "AND t.kind = ? " +
                         "ORDER BY sm.title, t.kind, p.movie_date";
            PreparedStatement pstmt = con.prepareStatement(sql);
            
            // 파라미터 바인딩 (쿼리 조건 추가)
            pstmt.setString(1, selectedDate);  // 날짜 조건
            pstmt.setString(2, movieTitle);    // 영화 제목 조건
            pstmt.setString(3, rating);        // 등급 조건
            pstmt.setString(4, kind);          // 상영 형태 조건
    
            ResultSet rs = pstmt.executeQuery();
    
            // 영화 정보를 저장할 맵
            Map<String, Movie> movieMap = new HashMap<>();
            while (rs.next()) {
                Integer res_id = rs.getInt("play_info_id");
                String title = rs.getString("title");
                String movieRuntime = rs.getString("runtime");
                String movieRating = rs.getString("rating");
                String poster = rs.getString("poster");
                String movieKind = rs.getString("kind");
                String section = rs.getString("section");
                Integer seat = rs.getInt("seat");
                LocalDate movieDate = rs.getDate("movie_date").toLocalDate();
                LocalTime startTimes = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
    
                // 시간을 받아와 문자열 및 형식 변환
                String date = movieDate.toString();
                String stime = startTimes.format(DateTimeFormatter.ofPattern("HH:mm"));
                String etime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    
                // movieKey에 여러 필드를 결합하여 고유하게 만듦
                String movieKey = title + "|" + movieRating + "|" + movieKind + "|" + movieRuntime + "|" + poster;
    
                if (!movieMap.containsKey(movieKey)) {
                    movieMap.put(movieKey, new Movie(res_id, title, movieRating, movieRuntime, poster, movieKind));
                }
    
                // 상영 정보를 추가
                movieMap.get(movieKey).addScreening(date, stime, etime, seat, section);
            }
    
            for (Movie movie : movieMap.values()) {
                // MovieListViewCell movieCell = new MovieListViewCell(movie);
                HBox movieCell = new HBox();
                movieCell.setSpacing(20);
                VBox movieInfo = new VBox();
                HBox layout = new HBox();
                movieInfo.setSpacing(10);
                layout.setSpacing(20);
    
                // 영화 정보 불러오기
                Label titleLabel = new Label("[" + movie.getRating() + "]" + movie.getTitle() + "(" + movie.getKind() + ")" + movie.getRuntime() + "분");
    
                // TablePane에 상영 시간 Button 추가
                FlowPane timeFlow = new FlowPane();
                timeFlow.setVgap(10);
                timeFlow.setHgap(10);
                timeFlow.setPrefWidth(200); // 가로 너비 고정
    
                // 상영 시간 버튼 생성
                for (Movie.Screening screening : movie.getScreenings()) {
                    VBox kindBox = new VBox();
                    Label kindLabel = new Label(screening.getSection());
                    Label seats = new Label(screening.getSeat() + "석");
                    Button timeButton = new Button(screening.getStartTime());
                    kindBox.getChildren().addAll(timeButton, kindLabel, seats);
    
                    timeButton.setOnAction(event -> {
                        // 선택된 시간에 맞는 동작을 추가
                        handleTimeSelection(movie.getTitle(), movie.getRating(), movie.getKind(), movie.getRuntime(), movie.getPoster(), screening.getSeat(), screening.getSection(), screening.getDate(), screening.getStartTime(), screening.getEndTime());
                    });
    
                    timeFlow.getChildren().add(kindBox); // FlowPane에 버튼 추가
                }
    
                layout.getChildren().addAll(movieInfo);
                movieInfo.getChildren().addAll(titleLabel, timeFlow);
                layout.setStyle("-fx-border-width:0 0 2px 0; -fx-border-color:#ccc; ");
                movieCell.getChildren().add(layout);
                movieList.getChildren().add(movieCell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	// 시간을 선택했을 때 처리하는 메서드
    private void handleTimeSelection(String movie, String rating,String kind, String runtime, String poster,Integer seat, String section, String date, String selectedTime, String selectedEndTime) {
        // 상영 시간을 선택했을 때의 동작을 여기에 구현합니다.
        // 예: 선택한 영화와 시간 정보를 다음 페이지로 전달하거나 처리
    	movieData.setSelectedMovieTitle(movie);
        movieData.setSelectedMovieRating(rating);
        movieData.setSelectedMovieType(kind);
        movieData.setSelectedMovieRuntime(runtime);
        movieData.setSelectedMoviePoster(poster);
        movieData.setSelectedMovieSeat(seat);
        movieData.setSelectedMovieSection(section);
        movieData.setSelectedMovieDate(date);
        movieData.setSelectedMovieStartTime(selectedTime);
        movieData.setSelectedMovieEndTime(selectedEndTime);
    	
        System.out.println("선택된 영화: " + movie);
        System.out.println("선택한 영화 관람 등급"+rating);
        System.out.println("선택된 영화 상영관 종류: "+ kind);
        System.out.println("선택된 영화 러닝 타임 "+runtime);
        System.out.println("선택된 영화 포스터 정보: "+poster);
        System.out.println("좌석수 , 상영관 : "+seat+","+section);
        System.out.println(date);
        System.out.println("선택된 영화 상영 시작 시간: " + selectedTime);
        System.out.println("선택된 영화 상영 종료 시간: "+ selectedEndTime);
        
        // 필요한 로직을 추가
    }
    private void NextPage(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasySelectMember.fxml"));
            Parent homeRoot = loader.load();
            EasySelectMember controller = loader.getController();
            //영화의 상영정보를 넘겨줌
            controller.initializeData(movieData);
			StackPane root = (StackPane) EasyTimePane.getScene().getRoot();
			root.getChildren().add(homeRoot);
			//전환 효과
			homeRoot.setTranslateX(650);
			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(homeRoot.translateXProperty(), 0);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(200),keyValue); //0.2초간 실행 좌에서 우로 이동...
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();//애니메이션 실행
			
//			Scene scene = EasyTimePane.getScene();
//			scene.getStylesheets().add(getClass().getResource("/fxml/css/choiseMovie.css").toExternalForm());
            // // 현재 창의 Stage를 가져옴
            // Stage stage = (Stage) home.getScene().getWindow();
            // Scene scene = new Scene(homeRoot); // 홈화면으로 전환

            // // 메뉴 화면으로 전환
            // stage.setScene(scene);
            // stage.show(); //화면에 출력
            

        } catch (Exception e) {
            e.printStackTrace();
            // 예외 발생 시 사용자에게 경고창 표시
        }
    }
    private void BackPage(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyChoiceMovie.fxml"));
            Parent homeRoot = loader.load();
            // 현재 창의 Stage를 가져옴
            Stage stage = (Stage) home.getScene().getWindow();
            Scene scene = new Scene(homeRoot); // 홈화면으로 전환

            // 메뉴 화면으로 전환
            stage.setScene(scene);
            stage.show(); //화면에 출력
            

        } catch (Exception e) {
            e.printStackTrace();
            // 예외 발생 시 사용자에게 경고창 표시
        }
    }
    private void BackHome(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyMonitor.fxml"));
            Parent homeRoot = loader.load();
            // 현재 창의 Stage를 가져옴
            Stage stage = (Stage) home.getScene().getWindow();
            Scene scene = new Scene(homeRoot); // 홈화면으로 전환

            // 메뉴 화면으로 전환
            stage.setScene(scene);
            stage.show(); //화면에 출력
            

        } catch (Exception e) {
            e.printStackTrace();
            // 예외 발생 시 사용자에게 경고창 표시
        }
    }
    public void loadData(Map<String, Movie> selectedData) {
        StringBuilder sb = new StringBuilder(); // 출력할 문자열을 저장할 StringBuilder

        for (Movie movie : selectedData.values()) {
            // 각 Movie 객체에서 필요한 정보를 가져옴
            String rating = movie.getRating();
            String title = movie.getTitle();
            String kind = movie.getKind();

            // 원하는 형식으로 문자열을 조합: [rating] title(kind)
            sb.append("[").append(rating).append("] ")
            .append(title).append(" (").append(kind).append(")\n");
        }

        // Label이나 TextField 등에 텍스트로 설정
        MovieInfo.setText(sb.toString());

        
    }
}
