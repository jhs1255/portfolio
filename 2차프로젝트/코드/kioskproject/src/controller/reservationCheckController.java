package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class reservationCheckController implements Initializable {
	@FXML private BorderPane rescheckPane;
	@FXML private Button home;
	@FXML private Button btnOk;
	@FXML private Label setRating;
	@FXML private Label setTitle;
	@FXML private Label setType;
	@FXML private Label setRuntime;
	@FXML private Label setDate;
	@FXML private Label setTime;
	@FXML private Label setTheater;
	@FXML private Label setSeats;
	@FXML private Label setPeople;
	@FXML private Label setRandom;
	@FXML private ImageView setPoster;
	
	private MovieData resMovie;
	private String resSeat;
	private String randomNum;
	private List<String> Details;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		home.setOnAction(event->switchHome(event));
		btnOk.setOnAction(event->switchHome(event));
	}
	
	public void initalizeData(MovieData resShowMovie, String seat, String resNum ,List<String> PeopleDetails) {
		this.resMovie = resShowMovie;
		this.resSeat = seat;
		this.randomNum = resNum;
		this.Details = PeopleDetails;
		
		if(resMovie != null) {
			setRating.setText(resMovie.getSelectedMovieRating());
			setTitle.setText(resMovie.getSelectedMovieTitle());
			setType.setText(resMovie.getSelectedMovieType());
			setRuntime.setText(resMovie.getSelectedMovieRuntime());
			setDate.setText(resMovie.getSelectedMovieDate());
			setTime.setText(resMovie.getSelectedMovieStartTime()+" ~ "+resMovie.getSelectedMovieEndTime());
			setTheater.setText(resMovie.getSelectedMovieSection());
			setSeats.setText(resSeat);
			setPeople.setText(String.join(", ", Details));
			setRandom.setText(randomNum);
			
			Image image;
            if (resMovie.getSelectedMoviePoster().startsWith("http://") || resMovie.getSelectedMoviePoster().startsWith("https://")) {
                // URL인 경우
                image = new Image(resMovie.getSelectedMoviePoster()); // URL로부터 이미지 로드
            } else {
                // 로컬 파일인 경우
                File file = new File(resMovie.getSelectedMoviePoster());
                image = new Image(file.toURI().toString());
            }
			
//			File file = new File(resMovie.getSelectedMoviePoster());
//			Image image = new Image(file.toURI().toString());
			setPoster.setImage(image);
			setPoster.setFitHeight(230);
            setPoster.setFitWidth(180);
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
}
