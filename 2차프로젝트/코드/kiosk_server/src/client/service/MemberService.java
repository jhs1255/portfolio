package client.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import classLoader.Connect;
import client.dto.MemberDto;
import client.dto.Response;
import enumcode.StatusCode;

public class MemberService {
	//할인을 위해 사용
    public String getMemberInformation(String phoneNumber){
    	//포인트를 가져옵니다.
    	Connection connection = Connect.getConnection();
	    String sql = "select point from user where phone=?";
	    MemberDto memberDto = null;
	    try {
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        pstmt.setString(1, phoneNumber);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) { // 결과가 있을 때
	            memberDto = new MemberDto(rs.getInt("point"));
	        } else { // 결과가 없을 때
	        	Response response = new Response();
	            response.setStatusCode(StatusCode.NOT_FOUND.getStatusCode());
	            response.setBody(memberDto);
	            return response.responseBuild();
	        }
//	        if(rs.next()){
//	            int available = rs.getInt("point");
//	            MemberDto memberDto = new MemberDto(phoneNumber , available);
//	                    //전화번호
//	            Response response = new Response();
//	            response.setBody(memberDto);
//	            response.setStatusCode(StatusCode.SUCCESS.getStatusCode());
//	            return response.responseBuild();
//	        }
//	        else {
//	            MemberDto memberDto = new MemberDto(phoneNumber, 0);
//	            Response response = new Response();
//	            response.setStatusCode(StatusCode.SUCCESS.getStatusCode());
//	            response.setBody(memberDto);
//	            return response.responseBuild();
//	        }
	    }catch (Exception e){
	    	System.out.println("오류발생..");
			e.printStackTrace();
	    }
	    Response response = new Response();
        response.setStatusCode(StatusCode.SUCCESS.getStatusCode());
        response.setBody(memberDto); // MemberDto를 setBody에 설정
        return response.responseBuild();
    }
    
    //적립을 위해 사용
    public String getPhoneVaild(String phoneNumber) {
    	Connection connection = Connect.getConnection();
	    String sql = "select count(*) from user where phone=?";
	    Response response = new Response();
	    try {
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        pstmt.setString(1, phoneNumber);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next() && rs.getInt(1) > 0) { // 전화번호가 존재할 때
	            response.setStatusCode(StatusCode.SUCCESS.getStatusCode());
	            response.setBody(null); // 필요에 따라 null 또는 다른 값을 설정
	        } else { // 전화번호가 존재하지 않을 때
	            response.setStatusCode(StatusCode.NOT_FOUND.getStatusCode());
	            response.setBody(null);
	        }
	    } catch (Exception e) {
	        System.out.println("오류발생..");
	        e.printStackTrace();
	        response.setStatusCode(StatusCode.BAD_REQUEST.getStatusCode());
	        response.setBody(null);
	    }
	    return response.responseBuild();

    }

}
