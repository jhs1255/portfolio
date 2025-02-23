package client.controller;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;

import client.service.MemberService;
import client.service.PlayInfoService;
import client.service.ReservationService;

public class MainController {
	private final PlayInfoService playInfoService = new PlayInfoService();
	private final MemberService memberService = new MemberService();
	private final ReservationService reservationService = new ReservationService();
	//영화 정보 불러오는 서비스를 처리하는 곳...
    public String getPlayInfo(LocalDateTime time) throws JsonProcessingException {
        String info = playInfoService.getPlayInfo(time);
        return info;
    }

    public String getMemberInfo(String phoneNumber) throws JsonProcessingException{
        String response = memberService.getMemberInformation(phoneNumber);
        return response;
    }
    
    public String getPrintInfo(String phoneNumber) throws JsonProcessingException {
    	String response = ReservationService.getPrintInfo(phoneNumber);
    	return response;
    }
    
    public String getPrintNumInfo(String resNumber) throws JsonProcessingException {
    	String response = ReservationService.getPrintNumInfo(resNumber);
    	return response;
    }
    
    public String getPhoneVaild(String phoneNumber) {
    	String response = memberService.getPhoneVaild(phoneNumber);
    	return response;
    }
    
}
