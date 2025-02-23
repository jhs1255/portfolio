package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dto.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegisterController {

    // MovieRegister.fxml에 대한 필드
    @FXML
    private VBox movieListVBox;

    @FXML
    private Button registerButton;

    @FXML
    private ScrollPane scrollPane;

    // MovieRegisterPopup.fxml에 대한 필드
    @FXML
    private TextField titleField;

    @FXML
    private TextField runtimeField;

    @FXML
    private TextField ratingField;
    
    @FXML
    private ComboBox<String> kindComboBox;

    @FXML
    private Button submitButton;
    
    @FXML
    private Button backToMenuButton;

    // 현재 선택된 HBox와 영화 제목을 추적하는 변수
    private HBox selectedMovieBox;
    private Integer selectedMovieId;
    private String selectedMovieTitle;  // 선택된 영화의 제목을 저장할 변수
    private Integer selectedMovieRuntime; //선택된 영화의 상영시간을 저장할 변수
    private String selectedMovieRating;// 선택된 영화의 상영등급을 저장할 변수
    private String selectedMoviePoster;
    
//	String url = "jdbc:mysql://localhost:3306/kiosk";
	private Connection con;

    // MovieRegister.fxml에서 영화 데이터를 로드하기 위한 초기화 메서드
    public void initialize() {
    	con = DBConnection.getConnection();

        if (con == null) {
            System.out.println("DB 연결 실패. 영화 데이터를 로드할 수 없습니다.");
            return;
        }
//        try {
//    		Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("데이터베이스 연결중...");
//			con = DriverManager.getConnection(url,"root","root1234");
//			System.out.println("연결성공");
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    	}
        
        if (movieListVBox == null) {
            System.out.println("movieListVBox is null");
        } else {
            loadMovieData();
        }
    }

    // VBox에 샘플 영화 데이터를 로드하는 메서드 (MovieRegister.fxml용)
    public void loadMovieData() {
    	try {
    		String sql = "SELECT movie_id,title,poster,runtime,rating FROM movies";
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Integer movie_id = rs.getInt("movie_id");
				String title = rs.getString("title");
				String poster = rs.getString("poster");
				Integer runtime = rs.getInt("runtime");
				String rating = rs.getString("rating");
				
				HBox movieBox = new HBox();
				movieBox.setStyle("-fx-border-color: #ccc;");
				movieBox.setPrefHeight(100);
				movieBox.setPrefWidth(200);
				
				Image image;
	            if (poster.startsWith("http://") || poster.startsWith("https://")) {
	                // URL인 경우
	                image = new Image(poster); // URL로부터 이미지 로드
	            } else {
	                // 로컬 파일인 경우
	                File file = new File(poster);
	                image = new Image(file.toURI().toString());
	            }
	            ImageView imageview = new ImageView(image);
	            imageview.setFitHeight(70);
	            imageview.setFitWidth(100);
				
				Label movieTitle = new Label(title);
				movieTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
				movieBox.getChildren().addAll(imageview,movieTitle);
				movieBox.setOnMouseClicked(event -> handleMovieSelection(movieBox, movieTitle.getText(),runtime,rating,poster,movie_id));
				movieListVBox.getChildren().add(movieBox);
				
			}
			
			con.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    // 영화 선택을 처리하는 메서드 (선택된 movieBox 강조 표시)
    private void handleMovieSelection(HBox movieBox, String movieTitle, Integer runtime, String rating, String poster, Integer movie_id) {
        // 이미 선택된 영화가 있으면 강조 표시를 제거
        if (selectedMovieBox != null) {
            selectedMovieBox.setStyle(""); // 이전에 선택된 영화의 스타일을 초기화
        }

        // 새로운 movieBox를 선택하고 강조 스타일 적용
        selectedMovieBox = movieBox;
        selectedMovieId = movie_id;
        selectedMovieTitle = movieTitle;  // 선택된 영화 제목을 저장
        selectedMovieRuntime = runtime; // 선택된 러닝타임을 저장
        selectedMovieRating = rating; // 선택된 상영등급을 저장
        selectedMoviePoster = poster; // 선택된 영화의 포스터를 저장
        selectedMovieBox.setStyle("-fx-border-color: blue; -fx-border-width: 3px; -fx-border-radius: 5px;");
    }

    // '영화 등록하기' 버튼 클릭 이벤트 처리 (MovieRegister.fxml)
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        if (selectedMovieTitle == null) {
            // 영화가 선택되지 않았을 경우 경고 메시지 출력
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("등록할 영화를 먼저 선택하세요.");
            alert.showAndWait();
            return;
        }

        try {
            // FXML 파일을 리소스 경로에서 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MovieRegisterPopup.fxml"));
            Parent root = loader.load();

            // 팝업의 컨트롤러를 가져옴
            RegisterController popupController = loader.getController();

            // 선택한 영화의 제목을 팝업 창의 제목 필드에 설정
            popupController.setMovieTitle(selectedMovieTitle);
            
            popupController.setMovieId(selectedMovieId);
            
            popupController.setMovieRuntime(selectedMovieRuntime);
            
            popupController.setMovieRating(selectedMovieRating);
            
            popupController.setMoviePoster(selectedMoviePoster);
            
         //팝업 컨트롤러에서 ComboBox 초기화
            popupController.resetComboBox();  // 팝업 창 내에서 ComboBox를 초기화

            Stage popupStage = new Stage();
            popupStage.setTitle("Movie Registration");
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(registerButton.getScene().getWindow());

            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            popupStage.show(); // 팝업을 띄움
        } catch (IOException e) {
            e.printStackTrace(); // 콘솔에 예외 메시지 출력
            // 예외 발생 시 사용자에게 경고창 띄우기
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("팝업창을 불러올 수 없습니다");
            alert.setContentText("영화 등록 창을 여는 도중 오류가 발생했습니다.");
            alert.showAndWait();
        }
    }
    
    public void resetComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList("2D", "3D", "4D", "4DX", "IMAX");
        kindComboBox.setItems(options);  // ComboBox에 선택 항목 추가
        kindComboBox.setValue("2D");  // 기본 선택값 설정
    }

    // 팝업창에서 영화 제목을 설정하는 메서드
    public void setMovieTitle(String movieTitle) {
        titleField.setText(movieTitle);
        titleField.setEditable(false);  // 제목을 읽기 전용으로 설정
    }
    
    // 팝업창에서 러닝타임을 설정하는 메소드
    public void setMovieRuntime(Integer runtime) {
    	runtimeField.setText(runtime != null ? runtime.toString() : "");
    }
    
    // 팝업창에서 상영등급을 설정하는 메소드
    public void setMovieRating(String rating) {
    	 ratingField.setText(rating != null ? rating : "");
    }
    
    public void setMoviePoster(String poster) {
    	this.selectedMoviePoster = poster;
    }
    
    public void setMovieId(Integer movie_id) {
    	this.selectedMovieId = movie_id;
    }

    // '등록 완료' 버튼 클릭 이벤트 처리 (MovieRegisterPopup.fxml)
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        String title = titleField.getText();
        String runtime = runtimeField.getText();
        String rating = ratingField.getText();
        String selectedKind = kindComboBox.getValue();
        // 중복 확인 쿼리
        try {
            String checkSql = "SELECT COUNT(*) FROM showmovie WHERE title = ? AND movietype = ?";
            PreparedStatement checkPstmt = con.prepareStatement(checkSql);
            checkPstmt.setString(1, title);
            checkPstmt.setString(2, selectedKind);
            ResultSet rs = checkPstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // 중복된 영화가 존재하는 경우
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("중복된 영화가 존재합니다. 다른 영화로 등록해주세요.");
                alert.showAndWait();

                // 팝업 창을 닫지 않고 등록 창으로 돌아가게 함
                return; // 메서드 종료
            }
            
    		String sql = "INSERT INTO showmovie(movie_id, title, runtime, rating, movietype, poster) VALUES(?,?,?,?,?,?)";
    		PreparedStatement pstmt = con.prepareStatement(sql);
    		pstmt.setInt(1, selectedMovieId);
    		pstmt.setString(2, title);
    		pstmt.setString(3, runtime);
    		pstmt.setString(4, rating);
    		pstmt.setString(5, selectedKind);
    		pstmt.setString(6, selectedMoviePoster); // 포스터 경로 저장
    	    pstmt.executeUpdate(); // 쿼리 실행
    		pstmt.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
        
        // 입력된 영화 데이터를 출력하거나 저장 (여기서 DB에 저장할 수 있음)
        System.out.println("영화 제목: " + title);
        System.out.println("러닝타임: " + runtime);
        System.out.println("상영등급: " + rating);
        System.out.println("상영 종류: " + selectedKind);

        // 팝업 창을 닫기
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
    
    // 메뉴로 돌아가는 버튼 클릭 이벤트 처리
    @FXML
    private void handleBackToMenuAction(ActionEvent event) {
        try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminMenuPopup.fxml"));
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
