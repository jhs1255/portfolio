package controller;

import java.io.IOException;
import java.util.List;

import dto.ReservationDAO;
import dto.ReservationDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ReservationListController {
	@FXML private Text rating, movieTitle, movieType, runTime, movieTime, movieDate, section, seats, Details, resNum;
	@FXML private VBox reservationListContainer;
	
    @FXML
    private Button printButton;

    @FXML
    private Rectangle reservationBorder;

    @FXML
    private AnchorPane reservationItem;

    private boolean isSelected = false; // 항목이 선택되었는지 여부를 저장
    
    private ReservationDAO reservationDAO = new ReservationDAO();
    
 // 여러 예매 정보 표시 메서드
 // 예약 정보를 화면에 표시하는 메서드
    public void setReservationDetails(List<ReservationDTO> reservations) {
        reservationListContainer.getChildren().clear(); // 기존 내용 삭제

        if (reservations.size() == 1) {
            // 단일 예약의 경우
            ReservationDTO reservation = reservations.get(0);
            movieTitle.setText(reservation.getMovieTitle());
            rating.setText(reservation.getMovieRating());
            movieType.setText(reservation.getMovieType());
            runTime.setText(reservation.getMovieRuntime() + "분");
            movieDate.setText(reservation.getMovieDate().toString());
            movieTime.setText(reservation.getMovieStart().toString() + "~" + reservation.getMovieEnd().toString());
            section.setText(reservation.getMovieTheater());
            seats.setText(String.join(", ", reservation.getSeats()));  // 좌석 리스트를 쉼표로 구분하여 표시
            resNum.setText(reservation.getRandomNumber());
            Details.setText("인원 수: " + String.join(", ", reservation.getPeople()));
        } else {
            // 여러 예약 정보 표시
            for (ReservationDTO reservation : reservations) {
                Text reservationText = new Text(
                    "예매번호: " + reservation.getRandomNumber() + "\n" +
                    "영화 제목: " + reservation.getMovieTitle() + "\n" +
                    "관람 등급: " + reservation.getMovieRating() + "\n" +
                    "영화 장르: " + reservation.getMovieType() + "\n" +
                    "상영 시간: " + reservation.getMovieRuntime() + "분\n" +
                    "예매 날짜: " + reservation.getMovieDate().toString() + "\n" +
                    "예매 시간: " + reservation.getMovieStart().toString() + "~" + reservation.getMovieEnd().toString() + "\n" +
                    "구역: " + reservation.getMovieTheater() + "\n" +
                    "좌석: " + String.join(", ", reservation.getSeats()) + "\n" +
                    "인원 수: " + String.join(", ", reservation.getPeople())
                );

                reservationListContainer.getChildren().add(reservationText); // VBox에 추가
            }
        }
    }

    //홈 버튼
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            // FXML 파일 로드 (kiosk.fxml로 이동)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kioskMain.fxml"));
            Parent homeView = loader.load();

            // 현재 스테이지 가져오기
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // 새로운 씬으로 전환
            Scene scene = new Scene(homeView);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // 에러 발생 시 콘솔에 출력
        }
    }

    // 예매 항목 클릭 시 출력 버튼 활성화 및 테두리 변경
    @FXML
    private void handleReservationItemClick(MouseEvent event) {
        if (isSelected) {
            // 이미 선택된 상태라면 선택을 해제
            reservationBorder.setStroke(Color.web("#b7b7b7")); // 테두리를 회색으로 변경
            printButton.setDisable(true); // 출력 버튼 비활성화
            isSelected = false; // 선택 상태를 해제
        } else {
            // 선택되지 않은 상태라면 선택
            reservationBorder.setStroke(Color.RED); // 테두리를 빨간색으로 변경
            printButton.setDisable(false); // 출력 버튼 활성화
            isSelected = true; // 선택 상태로 변경
        }
    }

    @FXML
    private void handlePrintButtonAction(ActionEvent event) {
        try {
            // FXML 파일 로드 (3-1.ReservationPrintPopup.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/3-1.ReservationPrintPopup.fxml"));
            Parent printPopupView = loader.load();

            // 새로운 스테이지 생성 (팝업 창)
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // 모달로 설정 (현재 창을 블록함)
            popupStage.setTitle("예매 내역 출력"); // 팝업 창 타이틀 설정
            popupStage.setScene(new Scene(printPopupView));

            // 부모 창 설정
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            popupStage.initOwner(parentStage); // 부모 창을 팝업의 소유자로 설정

            popupStage.showAndWait(); // 팝업 창을 띄움

        } catch (IOException e) {
            e.printStackTrace(); // 오류 발생 시 스택 트레이스 출력
        }
    }
    
    
}
