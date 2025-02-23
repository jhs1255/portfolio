package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dto.MovieData;
import dto.ReservationDAO;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class buyController implements Initializable {
	@FXML StackPane buyPane;
	@FXML Button home;
	@FXML Button usePoint;
	@FXML Button savePoint;
	@FXML Button purChase;
	
	@FXML ImageView initPoster;
	@FXML Label initTitle;
	@FXML Label initDate;
	@FXML Label initTime;
	@FXML Label initTheater;
	@FXML Label initPeople;
	@FXML Label resNum;
	@FXML Label totalPrice;
	private List<String> PeopleDetails;
	
	private MovieData selectMovieData; //선택한 영화정보를 가져올 변수
	private String seatString;
	private Integer toPrise;
	private String randNum;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		home.setOnAction(event->switchHome(event));
		usePoint.setOnAction(event->usePointPopup(event));
		savePoint.setOnAction(event->savePointPopup(event));
		purChase.setOnAction(event->rescheck(event));
	}
	
	// 선택한 영화 정보를 초기화하는 메서드
    public void initializeData(MovieData movieData , String seats, Integer Price, List<String> PeopleDetails, String RandomNumber) {
        this.selectMovieData = movieData;
        this.seatString = seats;
        this.toPrise = Price;
        this.PeopleDetails = PeopleDetails;
        this.randNum = RandomNumber;
        updateUI();
    }
    
    private void updateUI() {
    	if (selectMovieData != null) {
            initTitle.setText("("+selectMovieData.getSelectedMovieRating()+") "+selectMovieData.getSelectedMovieTitle()+" ("+selectMovieData.getSelectedMovieType()+") "+ selectMovieData.getSelectedMovieRuntime()+"분");
            initDate.setText(selectMovieData.getSelectedMovieDate());
            initTime.setText(selectMovieData.getSelectedMovieStartTime()+" ~ "+selectMovieData.getSelectedMovieEndTime());
            initTheater.setText(selectMovieData.getSelectedMovieSection()+" "+seatString);
            initPeople.setText(String.join(", ", PeopleDetails));
            totalPrice.setText(String.valueOf(toPrise)+" 원");
            resNum.setText(randNum);
            System.out.println(selectMovieData.getSelectedMovieTitle());
            // 포스터 이미지 설정
            File file = new File(selectMovieData.getSelectedMoviePoster());
            Image image = new Image(file.toURI().toString());
            initPoster.setImage(image);
            initPoster.setFitHeight(230);
            initPoster.setFitWidth(180);
        }
    }
	
	//메인화면으로 이동
	private void switchHome(ActionEvent event) {
		try {
            // FXML 파일을 로드하여 메뉴 화면으로 전환
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
            Parent homeRoot = loader.load();
            // 현재 창의 Stage를 가져옴
            Stage stage = (Stage) home.getScene().getWindow();
            Scene scene = new Scene(homeRoot);
            // 메뉴 화면으로 전환
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 발생 시 사용자에게 경고창 표시
        }
	}
		
	// 포인트 사용 팝업
	public void usePointPopup(ActionEvent event) {
		//현재 스테이지 정보 가져오기
		Stage usePointPopupStage = (Stage) usePoint.getScene().getWindow();
		//팝업을 띄워줄 새로운 스테이지 생성
		Stage pop = new Stage(StageStyle.DECORATED);
		pop.initModality(Modality.WINDOW_MODAL);
		pop.initOwner(usePointPopupStage);
		//팝업창 불러오기
		try {
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("/fxml/PointUsingPopup.fxml"));
			Scene scene = new Scene(root);
			pop.setScene(scene);
			pop.setTitle("포인트 사용");
			pop.setResizable(false);// 창 조절 차단
			// 팝업 보여주기
			pop.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 포인트 적립 팝업
	public void savePointPopup(ActionEvent event) {
		//현재 스테이지 정보 가져오기
		Stage savePointPopupStage = (Stage) usePoint.getScene().getWindow();
		//팝업을 띄워줄 새로운 스테이지 생성
		Stage pop = new Stage(StageStyle.DECORATED);
		pop.initModality(Modality.WINDOW_MODAL);
		pop.initOwner(savePointPopupStage);
		//팝업창 불러오기
		try {
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("/fxml/PointSavingPopup.fxml"));
			Scene scene = new Scene(root);
			pop.setScene(scene);
			pop.setTitle("포인트 적립");
			pop.setResizable(false);// 창 조절 차단
			// 팝업 보여주기
			pop.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//예약확인 창으로 이동
	private void rescheck(ActionEvent event) {
		try {
			// DB에 예매 정보를 저장하는 로직
	        ReservationDAO reservationDAO = new ReservationDAO();
	        reservationDAO.saveReservation(selectMovieData, seatString, toPrise, randNum, PeopleDetails);
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reservationCheck.fxml"));
	        Parent rescheckstage = loader.load();
	        //컨트롤러 불러오기
	        reservationCheckController resController = loader.getController();
	        resController.initalizeData(selectMovieData,seatString,randNum,PeopleDetails);
	        
			StackPane root = (StackPane) buyPane.getScene().getRoot();
			root.getChildren().add(rescheckstage);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
