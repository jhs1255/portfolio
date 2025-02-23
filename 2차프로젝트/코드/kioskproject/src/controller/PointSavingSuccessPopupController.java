package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dto.MemberDto;
import dto.PlayInfoDto;
import dto.ReservationDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PointSavingSuccessPopupController implements Initializable {
	@FXML private Text savingPoint;
	
	private PlayInfoDto playInfoDto;
    private ReservationDTO reserveDto;
    private MemberDto memberDto;
	private buyController buyController;
	
	public PlayInfoDto getPlayInfoDto() {
		return playInfoDto;
	}

	public void setPlayInfoDto(PlayInfoDto playInfoDto) {
		this.playInfoDto = playInfoDto;
	}

	public ReservationDTO getReserveDto() {
		return reserveDto;
	}

	public void setReserveDto(ReservationDTO reserveDto) {
		this.reserveDto = reserveDto;
	}



	public MemberDto getMemberDto() {
		return memberDto;
	}



	public void setMemberDto(MemberDto memberDto) {
		this.memberDto = memberDto;
	}



	public buyController getBuyController() {
		return buyController;
	}



	public void setBuyController(buyController buyController) {
		this.buyController = buyController;
	}

	public void setSavePoint(int saving) {
	    if (savingPoint != null) {
	        savingPoint.setText(String.valueOf(saving) + " 원");
	    } else {
	        System.out.println("savePoint is null!");
	    }
	}

	public void init() {
		if (buyController == null) {
	        System.err.println("buyController is null in PointSavingSuccessPopupController.");
	    } else {
	        System.out.println("Buy controller initialized: " + buyController);
	    }
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (savingPoint == null) {
	        System.out.println("savePoint is null in initialize.");
	    }
	}
	
	//확인 버튼
	@FXML
	public void handleBtnOk(ActionEvent event) {
		// 현재 팝업 창을 닫음
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
	}

}
