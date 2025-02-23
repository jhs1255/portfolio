package controller;

import java.io.IOException;
import java.util.List;

import dto.ReservationDAO;
import dto.ReservationDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class RefundListController {

	@FXML private Text rating, movieTitle, movieType, runTime, movieTime, movieDate, section, seats, Details, resNum;
	
    @FXML
    private VBox reservationListContainer; // 예매 내역 목록을 담는 컨테이너

    @FXML
    private Button printButton; // 출력 버튼

    @FXML
    private AnchorPane rootPane; // 전체 화면의 루트 패널

    private AnchorPane selectedReservation = null; // 선택된 예매 항목을 저장


    private ReservationDAO reservationDAO = new ReservationDAO();
    
    // 예매 데이터를 화면에 표시하는 메서드
    public void setReservationDetails(String reservationNumber) {
        // 예매 데이터를 DAO에서 가져옴
        ReservationDTO reservation = reservationDAO.getReservationByNumber(reservationNumber);

        if (reservation != null) {
            // 가져온 데이터를 화면에 표시
            movieTitle.setText(reservation.getMovieTitle());
            rating.setText(reservation.getMovieRating());
            movieType.setText(reservation.getMovieType());
            runTime.setText(reservation.getMovieRuntime() + "분");
            movieDate.setText(reservation.getMovieDate().toString());
            movieTime.setText(reservation.getMovieStart().toString()+"~"+reservation.getMovieEnd().toString());

            // 좌석 정보 및 섹션 정보 설정
            section.setText(reservation.getMovieTheater());
            seats.setText(String.join(", ", reservation.getSeatList()));  // 좌석 리스트를 쉼표로 구분하여 표시

            // 기타 예매 정보
            resNum.setText(reservation.getRandomNumber());
            Details.setText("인원 수: " + String.join(", ", reservation.getPeopleList()));
        }
    }
    
 // 여러 예매 정보 표시 메서드
    public void setReservationDetails(List<ReservationDTO> reservations) {
        reservationListContainer.getChildren().clear(); // 기존 내용 삭제

        for (ReservationDTO reservation : reservations) {
            // 새로운 예약 항목 생성
            Text reservationText = new Text(
                "예매번호: " + reservation.getRandomNumber() + "\n" +
                "영화 제목: " + reservation.getMovieTitle() + "\n" +
                "관람 등급 " + reservation.getMovieRating() + "\n" +
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
    
    // 예매 내역 클릭 시 테두리 빨간색 변경 및 출력 버튼 활성화/비활성화
    @FXML
    private void handleReservationClick(MouseEvent event) {
        AnchorPane clickedReservation = (AnchorPane) event.getSource();
        Rectangle rect = (Rectangle) clickedReservation.lookup("Rectangle");

        // 이미 선택된 항목을 다시 클릭하면 선택 해제
        if (selectedReservation == clickedReservation) {
            rect.setStroke(Color.web("#b7b7b7")); // 원래 회색 테두리로 복구
            selectedReservation = null; // 선택된 항목 해제
            printButton.setDisable(true); // 버튼 비활성화
        } else {
            // 이전에 선택된 예매 항목이 있으면 원래 상태로 돌림
            if (selectedReservation != null) {
                Rectangle prevRect = (Rectangle) selectedReservation.lookup("Rectangle");
                prevRect.setStroke(Color.web("#b7b7b7")); // 원래 회색 테두리로 복구
            }

            // 새로운 항목 선택
            rect.setStroke(Color.RED); // 빨간 테두리로 설정
            selectedReservation = clickedReservation; // 선택된 예매 항목 저장
            printButton.setDisable(false); // 버튼 활성화
        }
    }

    // 환불 버튼 클릭 시 호출
    @FXML
    private void handleRefundButtonAction(ActionEvent event) {
        if (selectedReservation != null) {
            // 여기서는 임의로 티켓이 출력된 상태인지 여부를 확인
            // 나중에 백엔드와 연결될 때는 실제 데이터베이스나 API에서 상태 확인
            boolean isTicketPrinted = checkIfTicketIsPrinted(); // 이 메서드는 임의로 만듦

            if (isTicketPrinted) {
                // 이미 티켓이 출력된 경우
                showRefundFailPopup();
            } else {
                // 티켓이 출력되지 않은 경우
                showRefundAskPopup();
            }
        }
    }

    // 티켓 출력 여부 확인 (임시 메서드, 나중에 실제 백엔드 연결 필요)
    private boolean checkIfTicketIsPrinted() {
        // 임의로 티켓이 출력된 상태를 리턴
        // 나중에 데이터베이스나 API와 연동하여 실제 출력 여부 확인
        return false; // 예를 들어, false는 티켓이 출력되지 않음을 의미
    }

    // 티켓 출력된 경우 실패 팝업 띄우기 (4-3.RefundfailPopup)
    private void showRefundFailPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/4-3.RefundfailPopup.fxml"));
            Parent failPopupView = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("환불 실패");
            popupStage.setScene(new Scene(failPopupView));
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 티켓 출력되지 않은 경우 환불 확인 팝업 띄우기 
    @FXML
    private void showRefundAskPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/4-1.RefundAskPopup.fxml"));
            Parent refundAskPopup = loader.load();

            // 팝업 창 생성
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("환불 확인");

            // 부모 창 설정
            Stage parentStage = (Stage) rootPane.getScene().getWindow();
            popupStage.initOwner(parentStage);  // 부모 창을 설정

            popupStage.setScene(new Scene(refundAskPopup));
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 출력 버튼 클릭 시 출력 기능 구현 (현재는 단순 출력 메시지만)
    @FXML
    private void handlePrintButtonAction(ActionEvent event) {
        if (selectedReservation != null) {
            System.out.println("출력 팝업 띄우기");
            // 팝업 추가 구현..
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
}
