package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.Movie;
import dto.MovieData;
import dto.PlayInfoDto;
import dto.RequestCode;
import dto.RequestDto;
import dto.StatusCode;
import handleMessage.HandleFunction;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class choiseMovieController implements Initializable {
	@FXML private StackPane choiseMoviePane;
	
	@FXML private Button okCheckBtn;
	
	@FXML private Label ToDay; // 오늘 날짜 설정
	@FXML private Button home; // 홈버튼
	@FXML private Button nextPage; // 다음페이지 버튼
	@FXML private Button day1,day2,day3,day4,day5,day6,day7; // 날짜 선택버튼
	
	@FXML private VBox movieList;
	
	private DateTimeFormatter formatter,buttonformatter;
	MovieData movieData;
    
    // 모든 버튼을 배열로 관리
    private Button[] buttons;
	
//	String url = "jdbc:mysql://localhost:3306/kiosk";
//	private Connection con;
//	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		movieData = new MovieData();
//		try {
//    		Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("데이터베이스 연결중...");
//			con = DriverManager.getConnection(url,"root","root1234");
//			System.out.println("연결성공");
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    	}
//        
//        if (movieList == null) {
//            System.out.println("movieListVBox is null");
//        } else {
//            //loadMovieList();
//        }
		
		LocalDate today = LocalDate.now();
		formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)");
		buttonformatter = DateTimeFormatter.ofPattern("d"+"일"+"\n(E)");
		ToDay.setText(today.format(formatter)); // 오늘 날짜 불러옴
		
		day1.getStyleClass().add("selected");
		// 버튼 배열 초기화
        buttons = new Button[]{day1, day2, day3, day4, day5, day6, day7};
        
        // 오늘 날짜 기준 7일 버튼에 날짜 설정
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(today.plusDays(i).format(buttonformatter));
        }
		
        // 모든 버튼에 클릭 이벤트 핸들러 추가
        for (Button button : buttons) {
            button.setOnMouseClicked(event -> handleButtonClick(button));
        }
		
		home.setOnAction(event->switchHome(event)); // 홈화면으로 이동
		nextPage.setOnAction(event->switchNextPage(event));// 다음페이지로 이동
		
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
//		String selectedDate = selectedButton.getText();
		String selectedDate ="";
		// 선택한 버튼의 날짜에 맞춰 쿼리 실행
	    switch (selectedButton.getId()) {
	        case "day1":
	            selectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            break;
	        case "day2":
	            selectedDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            break;
	        case "day3":
	            selectedDate = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            break;
	        case "day4":
	            selectedDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            break;
	        case "day5":
	            selectedDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            break;
	        case "day6":
	            selectedDate = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            break;
	        case "day7":
	            selectedDate = LocalDate.now().plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            break;
	        default:
	            break;
	    }
		System.out.println("선택날짜: "+selectedDate);
		loadMovieList(selectedDate);
	}
	
	private void loadMovieList(String selectedDate) {
		// 이전에 추가된 정보를 초기화
	    movieList.getChildren().clear();
	 // 서버에 요청할 DTO 생성
	    RequestDto requestDto = new RequestDto();
	    requestDto.setRequestCode(RequestCode.GET_PLAYINFO_TIME);
	    requestDto.setBody(selectedDate);
	    HandleFunction handleFunction = new HandleFunction();
		String message = handleFunction.submit(requestDto);
		System.out.println(message);
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Map<String, Object> jsonMap = null;
			try {
				jsonMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//try
			//만약 성공했다면...
			Integer statusCode = (Integer) jsonMap.get("statusCode");
			if(statusCode == StatusCode.SUCCESS.getStatusCode()) {
				List<PlayInfoDto> list = null;
				list = objectMapper.convertValue(jsonMap.get("body"), new TypeReference<List<PlayInfoDto>>() {});
				
				// 영화 정보를 저장할 맵
		        Map<String, Movie> movieMap = new HashMap<>();
		        for(PlayInfoDto playDto: list) {
		        	Integer mov_id = playDto.getPlay_info_id();
		        	String title = playDto.getTitle();
		        	String runtime = playDto.getRuntime();
		        	String rating = playDto.getRating();
		        	String poster = playDto.getPoster();
		        	String kind = playDto.getKind();
		        	String section = playDto.getSection();
		        	Integer seat = playDto.getSeat();
		        	String movieDate = playDto.getMovieDate();
		        	String stime = playDto.getStartTimes();
		        	String etime = playDto.getEndTime();
		        	String movieKey = title + "|" + rating + "|" + kind + "|" +runtime +"|" + poster;
		        	if (!movieMap.containsKey(movieKey)) {
	                  movieMap.put(movieKey, new Movie(mov_id,title, rating, runtime, poster, kind));
		        	}//if

	              movieMap.get(movieKey).addScreening(movieDate, stime, etime, seat, section);
		        }//for
		        
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
		            posterImage.setFitHeight(230);
		            posterImage.setFitWidth(180);
					
					
					
					//영화정보 불러오기
					Label titleLabel = new Label("["+movie.getRating()+"]"+movie.getTitle()+"("+ movie.getKind() +")"+movie.getRuntime()+"분");//영화 정보 가져오기
					
					// TablePane에 상영 시간 Button 추가
			        FlowPane timeFlow = new FlowPane();
			        timeFlow.setVgap(10);
			        timeFlow.setHgap(10);
			        timeFlow.setPrefWidth(200); // 가로 너비 고정
			        
			        // 상영 시간 버튼 생성
			        for (Movie.Screening screening : movie.getScreenings()) {
			        	VBox kind = new VBox();
			        	Label kindLabel = new Label(screening.getSection());
			        	Label seats = new Label(screening.getSeat()+"석");
			            Button timeButton = new Button(screening.getStartTime());
			            kind.getChildren().addAll(timeButton,kindLabel,seats);
			            timeButton.setOnAction(event -> {
			                // 이곳에 선택된 시간에 맞는 동작을 추가할 수 있음
			                handleTimeSelection(movie.getRes_id(),movie.getTitle(),movie.getRating(),movie.getKind(), movie.getRuntime(), movie.getPoster(),screening.getSeat(), screening.getSection(),screening.getDate(),screening.getStartTime(), screening.getEndTime());
			            });
			            timeFlow.getChildren().add(kind); // FlowPane에 버튼 추가
			        }//for
			        
			        layout.getChildren().addAll(posterImage, movieInfo);
			        movieInfo.getChildren().addAll(titleLabel, timeFlow);
			        layout.setStyle("-fx-border-width:0 0 2px 0; -fx-border-color:#ccc; ");
			        movieCell.getChildren().add(layout);
	                movieList.getChildren().add(movieCell);
	            }//for
			}//if
			else {
				System.out.println("영화 리스트 가져오기 오류: " + jsonMap.get("errorMessage"));
			}//else
		}catch(Exception e) {
			e.printStackTrace();
		}//try
		
	}
	
	// 시간을 선택했을 때 처리하는 메서드
    private void handleTimeSelection(Integer play_Id, String movie, String rating,String kind, String runtime, String poster,Integer seat, String section, String date, String selectedTime, String selectedEndTime) {
        // 상영 시간을 선택했을 때의 동작을 여기에 구현합니다.
        // 예: 선택한 영화와 시간 정보를 다음 페이지로 전달하거나 처리
    	movieData.setSelectedPlay_info_id(play_Id);
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
    	
        System.out.println("선택된 상영정보 ID: "+play_Id);
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
	
	//홈으로 이동
	private void switchHome(ActionEvent event) {
		try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
            Parent homeRoot = loader.load();
            // 현재 창의 Stage를 가져옴
            Stage stage = (Stage) home.getScene().getWindow();
            Scene scene = new Scene(homeRoot); // 홈화면으로 전환

            // 메뉴 화면으로 전환
            stage.setScene(scene);
            stage.show(); //화면에 출력
            

        } catch (IOException e) {
            e.printStackTrace();
            // 예외 발생 시 사용자에게 경고창 표시
        }
	}
	
	//인원 선택 창으로 이동
	public void switchNextPage(ActionEvent event) {
		try {
			String Rating = movieData.getSelectedMovieRating();
			if(Rating.equals("19")) {
				// 팝업 창 생성
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/0-1.MovieGradePopup.fxml"));
	            Parent gradPopupStage = loader.load();
	            
	         // 팝업 컨트롤러 가져오기
	            MovieGradePopup popupController = loader.getController();
	            popupController.setParentController(this); // 현재 컨트롤러를 전달
	         // 새로운 Stage 생성
	            Stage popupStage = new Stage();
	            popupStage.setTitle("관람 경고");
	            popupStage.initModality(Modality.WINDOW_MODAL); // 모달 설정
	            popupStage.initOwner(choiseMoviePane.getScene().getWindow()); // 소유자 설정
	         // Scene 설정
	            Scene scene = new Scene(gradPopupStage);
	            popupStage.setScene(scene);
	            // 모달 창 표시
	            popupStage.show(); // 이 줄을 추가하세요.
	            
			}else {
				proceedNextPage();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//switchNextPage
	
	//다음 페이지 불러오는 코드
	public void proceedNextPage() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/selectMember.fxml"));
	        Parent selectMemberStage = loader.load();
            selectMemberController controller = loader.getController();
            //영화의 상영정보를 넘겨줌
            controller.initializeData(movieData);
			StackPane root = (StackPane) choiseMoviePane.getScene().getRoot();
			root.getChildren().add(selectMemberStage);
			//전환 효과
			selectMemberStage.setTranslateX(650);
			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(selectMemberStage.translateXProperty(), 0);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(200),keyValue); //0.2초간 실행 좌에서 우로 이동...
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();//애니메이션 실행
			
			Scene scene = choiseMoviePane.getScene();
			scene.getStylesheets().add(getClass().getResource("/fxml/css/choiseMovie.css").toExternalForm());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//proceedNextPage

}
