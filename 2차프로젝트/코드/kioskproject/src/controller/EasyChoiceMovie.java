package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dto.DBConnection;

// import com.mysql.cj.x.protobuf.MysqlxDatatypes.Scalar.String;

// import com.mysql.cj.x.protobuf.MysqlxDatatypes.Scalar.String;

import dto.Movie;
import dto.MovieData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class EasyChoiceMovie implements Initializable{

    public static String date;
    @FXML private StackPane choiseMoviePane;
    @FXML private Button home; // 홈버튼
    @FXML private Button nextPage; // 다음으로 버튼 
    @FXML private Button backBtn; // 이전으로 버튼
    @FXML private Label ToDay; // 날짜 라벨
    @FXML private VBox movieList; // 영화 목록 패널

    @FXML private Label selectedDateText; //선택된 날짜
    
    MovieData movieData;
//    String url = "jdbc:mysql://localhost:3306/kiosk";
    String selectedDate;
    Map<String, Movie> selectedData;
	private Connection con;
    private Movie selectedMovie;
    private String movieTitle;
    private String movieRating;
    private String movieKind;

    // public String setSelectedDate(String date) {
    //     selectedDateText.setText(date);
    //     System.out.println("선택된 날짜: " + date); // 로그로 값 확인
    //     return date;
    // }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        home.setOnAction(event->BackHome(event));// home 버튼
        backBtn.setOnAction(event->BackPage(event)); //이전으로 버튼
        nextPage.setOnAction(event->NextPage(event)); //다음으로 버튼

        movieData = new MovieData();
        selectedData = new HashMap<>();
        con = DBConnection.getConnection();
//		try {
//    		Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("데이터베이스 연결중...");
//			con = DriverManager.getConnection(url,"root","db2023");
//			System.out.println("연결성공");
//
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    	}
        
        if (movieList == null) {
            System.out.println("movieListVBox is null");
        } else {
            // loadMovieList();
        }
        
    }

    private void selectMovie(HBox movieCell, Movie movie, Map<String, Movie> movieMap) {
        // 선택된 영화의 스타일을 초기화
        movieList.getChildren().forEach(node -> node.setStyle("")); 
    
        // 선택된 영화의 스타일 변경 (배경색을 바꿈)
        movieCell.setStyle("-fx-background-color: #cce5ff;"); // 하늘색 배경
        selectedMovie = movie; // 선택된 영화 저장

        //누를 때 마다초기화
        selectedData.clear();
        selectedData.put(selectedMovie.getRating(), selectedMovie);
        selectedData.put(selectedMovie.getTitle(), selectedMovie);
        selectedData.put(selectedMovie.getKind(), selectedMovie);

        movieTitle=selectedMovie.getTitle();
        movieRating=selectedMovie.getRating();
        movieKind=selectedMovie.getKind();
        System.out.println(movieTitle+"|"+movieRating+"|"+movieKind);

        System.out.println(selectedData);
    }
    
	//영화 목록 출력
	public void loadMovieList(String date) {
        selectedDateText.setText(date);
        System.out.println("선택된 날짜: " + date); // 로그로 값 확인
        selectedDate=date;
		// 이전에 추가된 정보를 초기화
        movieList.getChildren().clear();
        //Play_info 테이블에서 상영정보를 가져와서 추가
		
		try {
			String sql = "SELECT p.play_Info_id, sm.title, sm.runtime, sm.rating, sm.poster, sm.movietype, t.kind, t.section,t.seat, p.movie_date, p.start_time, p.end_time " +
                "FROM play_info p " +
                "JOIN showmovie sm ON p.movie_id = sm.movie_id " +
                "JOIN theater t ON p.theater_id = t.theater_id " +
                "WHERE sm.movietype = t.kind AND p.movie_date = ? " +
                "ORDER BY sm.title, t.kind, p.movie_date";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, date);
			ResultSet rs = pstmt.executeQuery();
			// 영화 정보를 저장할 맵
            Map<String, Movie> movieMap = new HashMap<>();
			while(rs.next()) {
                int playInfoId = rs.getInt("play_Info_id"); // play_Info_id 가져오기
				String title = rs.getString("title");
                String runtime = rs.getString("runtime");
                String rating = rs.getString("rating");
                String poster = rs.getString("poster");
                String kind = rs.getString("kind");
                String section = rs.getString("section");
                Integer seat = rs.getInt("seat");
                LocalDate movieDate = rs.getDate("movie_date").toLocalDate();
                LocalTime startTimes = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                
                // 시간을 받아와 문자열 및 형식 변환
                // String date = movieDate.toString();
            	// String stime = startTimes.format(DateTimeFormatter.ofPattern("HH:mm"));
            	// String etime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            	
            	String movieKey = title + "|" + rating + "|" + kind + "|" +runtime +"|" + poster;
            	
            	if (!movieMap.containsKey(movieKey)) {
                    movieMap.put(movieKey, new Movie(playInfoId, title, rating, runtime, poster, kind));
                }

                // movieMap.get(movieKey).addScreening(date, stime, etime, seat, section);
            	
			}
			for (Movie movie : movieMap.values()) {
                //MovieListViewCell movieCell = new MovieListViewCell(movie);
				HBox movieCell = new HBox();
				movieCell.setSpacing(20);
				VBox movieInfo = new VBox();
				HBox layout = new HBox();
				movieInfo.setSpacing(10);
				layout.setSpacing(20);
				
				Image image;
	            if (movie.getPoster().startsWith("http://") || movie.getPoster().startsWith("https://")) {
	                // URL인 경우
	                image = new Image(movie.getPoster()); // URL로부터 이미지 로드
	            } else {
	                // 로컬 파일인 경우
	                File file = new File(movie.getPoster());
	                image = new Image(file.toURI().toString());
	            }
	            ImageView posterImage = new ImageView(image);
	            posterImage.setFitHeight(70);
	            posterImage.setFitWidth(100);
				
//				//포스터 이미지 불러오기
//				ImageView posterImage = new ImageView();
//				posterImage.setFitWidth(200);
//				posterImage.setFitHeight(150);
//				
//				File file = new File(movie.getPoster());
//				Image image = new Image(file.toURI().toString());
//				posterImage.setImage(image);
				
				//영화정보 불러오기
				Label titleLabel = new Label("["+movie.getRating()+"]"+movie.getTitle()+"("+ movie.getKind() +")"+movie.getRuntime()+"분");//영화 정보 가져오기
				Label ratingLabel = new Label("["+movie.getRating()+"]");//관람 등급 가져오기
				Label runtimeLabel = new Label(movie.getRuntime()+"분");//상영시간 가져오기
				
				// TablePane에 상영 시간 Button 추가
		        FlowPane timeFlow = new FlowPane();
		        timeFlow.setVgap(10);
		        timeFlow.setHgap(10);
		        timeFlow.setPrefWidth(200); // 가로 너비 고정
		        
		        // 상영 시간 버튼 생성
		        for (Movie.Screening screening : movie.getScreenings()) {
		        	Label kindLabel = new Label(movie.getKind()+"|"+screening.getSection()+"|"+screening.getSeat()+"석");
		            Button timeButton = new Button(screening.getStartTime());
		            timeButton.setOnAction(event -> {
		                // 이곳에 선택된 시간에 맞는 동작을 추가할 수 있음
		                // handleTimeSelection(movie.getTitle(),movie.getRating(),movie.getKind(), movie.getRuntime(), movie.getPoster(),screening.getSeat(), screening.getSection(),screening.getDate(),screening.getStartTime(), screening.getEndTime());
		            });
		            timeFlow.getChildren().addAll(kindLabel,timeButton); // FlowPane에 버튼 추가
		        }
		        
                posterImage.setOnMouseClicked(event -> selectMovie(movieCell, movie, movieMap)); //영화 포스터 선택했을 떄
                

		        layout.getChildren().addAll(posterImage, movieInfo);
		        movieInfo.getChildren().addAll(titleLabel, timeFlow);
		        layout.setStyle("-fx-border-width:0 0 2px 0; -fx-border-color:#ccc; ");
		        movieCell.getChildren().add(layout);
                movieList.getChildren().add(movieCell);
            }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

    private void NextPage(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyTime.fxml"));
            Parent nextPageRoot = loader.load();
            EasyTime EasyTimeCt = loader.getController();
            EasyTimeCt.loadData(selectedData);
            System.out.println(selectedData);
            EasyTimeCt.loadMovieList(selectedDate,movieTitle,movieRating,movieKind); //날짜와 영화를 가져가야함.
            System.out.println("NextPage버튼 : "+selectedDate);

            Stage stage = (Stage) nextPage.getScene().getWindow();
            Scene scene = new Scene(nextPageRoot);

            // 메뉴 화면으로 전환
            stage.setScene(scene);
            stage.show(); //화면에 출력
            

        } catch (Exception e) {
            e.printStackTrace();
            // 예외 발생 시 사용자에게 경고창 표시
        }
    }

    private void BackPage(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyDate.fxml"));
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
    
}
