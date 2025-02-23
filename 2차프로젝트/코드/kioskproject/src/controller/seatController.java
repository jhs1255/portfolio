package controller;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import dto.MovieData;
import dto.ReservationDAO;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class seatController implements Initializable {
	@FXML StackPane seatPane;
	@FXML GridPane seatGrid;
	@FXML Button home; // 홈버튼
	@FXML Button buyBtn; //결제하기 버튼
	
	private MovieData selectMovie;
	private Integer selectedMovieSeat;
	private Integer selectedPeople;
	private Integer selectedtotalPrice;
	private List<String> PeopleDetails;
	private Set<Button> selectedSeats = new HashSet<>(); // 선택된 좌석을 저장하는 Set

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		home.setOnAction(event->switchHome(event));// 홈화면으로 이동
		buyBtn.setOnAction(event->switchPayment(event));//결제화면으로 이동
	}
	
	public void initializeData(MovieData moviedata,Integer seatcount, Integer selectedNumberOfPeople, Integer selectedTotalPrice,List<String> selectedPeopleDetails) {
		this.selectMovie = moviedata;
		this.selectedMovieSeat = seatcount;
		this.selectedPeople = selectedNumberOfPeople;
		this.selectedtotalPrice = selectedTotalPrice;
		this.PeopleDetails = selectedPeopleDetails;
		System.out.println("선택한 좌석 수: " + selectedMovieSeat);
		System.out.println(selectedPeople);
		System.out.println(selectedtotalPrice);
		createSeats();
	}
	
	private void createSeats() {
		// 이미 예약된 좌석을 데이터베이스에서 가져옴
	    ReservationDAO reservationDAO = new ReservationDAO();
	    Set<String> reservedSeats = reservationDAO.getReservedSeats(selectMovie.getSelectedMovieTitle(), selectMovie.getSelectedMovieDate(), selectMovie.getSelectedMovieStartTime());
		//10줄 단위로 출력
	    for (int i = 0; i < (selectedMovieSeat + 9) / 10; i++) { // 필요한 행 수 계산
	        char rowLabel = (char) ('A' + i); // 행을 A, B, C로 설정
	        for (int j = 0; j < Math.min(10, selectedMovieSeat - i * 10); j++) {
	            String seatName = rowLabel + String.valueOf(j + 1);
	            Button seatButton = new Button(seatName); // 좌석 이름 설정

	            // 예약된 좌석인지 확인하고 비활성화 처리
	            if (reservedSeats.contains(seatName)) {
	                seatButton.setDisable(true); // 예약된 좌석이면 비활성화
	                seatButton.setStyle("-fx-background-color: gray;"); // 예약된 좌석은 회색으로 표시
	            } else {
	                seatButton.setOnAction(event -> handleSeatSelection(seatButton)); // 선택 가능 좌석 처리
	            }

	            seatGrid.add(seatButton, j, i); // 그리드에 추가
	        }
	    }
	}
	
	private void handleSeatSelection(Button seatButton) {
        // 좌석 선택 처리 로직
		if (selectedSeats.contains(seatButton)) {
            selectedSeats.remove(seatButton); // 이미 선택된 좌석이면 선택 해제
            seatButton.setStyle(""); // 기본 스타일로 변경
            enableAllSeats(); // 모든 좌석 다시 활성화
        } else {
            if (selectedSeats.size() < selectedPeople) { // 최대 선택 가능 좌석 수 체크
                selectedSeats.add(seatButton); // 좌석 추가
                seatButton.setStyle("-fx-background-color: lightblue;"); // 선택된 스타일로 변경
                if (selectedSeats.size() == selectedPeople) { // 최대 인원 수 도달 시
                    disableOtherSeats();
                }
            } else {
                // 최대 선택 수 도달 시 사용자에게 알림
                System.out.println("최대 좌석 수에 도달했습니다.");
            }
        }
        System.out.println(seatButton.getText() + " 선택됨.");
        // 선택된 좌석을 다른 UI나 리스트에 추가하는 등의 로직을 추가할 수 있음
    }
	//좌석 비활성화
	private void disableOtherSeats() {
	    for (Node node : seatGrid.getChildren()) {
	        if (node instanceof Button && !selectedSeats.contains(node)) {
	            node.setDisable(true); // 비활성화
	        }
	    }
	}
	//좌석 활성화
	private void enableAllSeats() {
	    for (Node node : seatGrid.getChildren()) {
	        if (node instanceof Button) {
	            node.setDisable(false); // 모든 좌석 활성화
	        }
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
	//예매 번호 16자리 생성
	private String generateRandomNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder randomNumber = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            randomNumber.append(random.nextInt(10)); // 0-9 사이의 숫자 추가
        }
        return randomNumber.toString();
    }
	
	//결제화면으로 이동
	private void switchPayment(ActionEvent event) {
		try {
			String randomNumber = generateRandomNumber();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/buy.fxml"));
	        Parent paymentstage = loader.load();
	        buyController controller = loader.getController();
	        StringBuilder selectedSeatsString = new StringBuilder();
            for (Button seat : selectedSeats) {
                selectedSeatsString.append(seat.getText()).append(", "); // 선택된 좌석 정보를 문자열로
            }
            // 마지막 ", " 제거
            if (selectedSeatsString.length() > 0) {
                selectedSeatsString.setLength(selectedSeatsString.length() - 2);
            }
            controller.initializeData(selectMovie, selectedSeatsString.toString(),selectedtotalPrice, PeopleDetails, randomNumber); // 선택한 좌석 정보 전달
			StackPane root = (StackPane) seatPane.getScene().getRoot();
			root.getChildren().add(paymentstage);
			
			//전환 효과
			paymentstage.setTranslateX(650);
			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(paymentstage.translateXProperty(), 0);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(200),keyValue); //0.2초간 실행 좌에서 우로 이동...
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();//애니메이션 실행
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
