package client.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import classLoader.Connect;
import client.dto.ReservationDTO;
import client.dto.Response;
import enumcode.StatusCode;

public class ReservationService {
	public static String getPrintInfo(String phoneNumber) throws JsonProcessingException{
		System.out.println("값을 가져오는 sql을 실행합니다...");
		Connection connection = Connect.getConnection();
		String sql = "SELECT " +
                "resNumber, " +
				"res_id, "+
                "movie_name, " +
                "rating, " +
                "movie_type, " +
                "movie_runtime, " +
                "movie_date, " +
                "movie_start, " +
                "movie_end, "+
                "movie_theater, " +
                "seats, " +
                "people, " +
                "status "+
                "FROM reservation " +
                "WHERE phone = ?";
		List<ReservationDTO> reservations = new ArrayList<>();
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
    		pstmt.setString(1, phoneNumber);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    			ReservationDTO reservationDTO = new ReservationDTO(
    					rs.getInt("res_id"),
    	    			rs.getString("movie_name"),
    	    			rs.getString("rating"),
    	    			rs.getString("movie_type"),
    	    			rs.getString("movie_runtime"),
    	    			rs.getDate("movie_date").toLocalDate().toString(),
    	    			rs.getTime("movie_start").toLocalTime().toString(),
    	    			rs.getTime("movie_end").toLocalTime().toString(),
    	    			rs.getString("movie_theater"),
    	    			rs.getString("seats"),
    	    			rs.getString("people"),
    	    			rs.getString("resNumber"),
    	    			rs.getString("status")
    			);
    			reservations.add(reservationDTO);
    		}
		}catch(Exception e) {
			System.out.println("오류발생..");
			e.printStackTrace();
		}
		
		Response response = new Response();
        response.setStatusCode(StatusCode.SUCCESS.getStatusCode());
        response.setBody(reservations);
        System.out.println(response.responseBuild());
        return response.responseBuild();
	}
	
	public static String getPrintNumInfo(String resNumber) throws JsonProcessingException{
		System.out.println("값을 가져오는 sql을 실행합니다...");
		Connection connection = Connect.getConnection();
		String sql = "SELECT " +
                "resNumber, " +
				"res_id, "+
                "movie_name, " +
                "rating, " +
                "movie_type, " +
                "movie_runtime, " +
                "movie_date, " +
                "movie_start, " +
                "movie_end, "+
                "movie_theater, " +
                "seats, " +
                "people, " +
                "status "+
                "FROM reservation " +
                "WHERE resNumber = ?";
		List<ReservationDTO> reservations = new ArrayList<>();
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
    		pstmt.setString(1, resNumber);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    			ReservationDTO reservationDTO = new ReservationDTO(
    					rs.getInt("res_id"),
    	    			rs.getString("movie_name"),
    	    			rs.getString("rating"),
    	    			rs.getString("movie_type"),
    	    			rs.getString("movie_runtime"),
    	    			rs.getDate("movie_date").toLocalDate().toString(),
    	    			rs.getTime("movie_start").toLocalTime().toString(),
    	    			rs.getTime("movie_end").toLocalTime().toString(),
    	    			rs.getString("movie_theater"),
    	    			rs.getString("seats"),
    	    			rs.getString("people"),
    	    			rs.getString("resNumber"),
    	    			rs.getString("status")
    			);
    			reservations.add(reservationDTO);
    		}
		}catch(Exception e) {
			System.out.println("오류발생..");
			e.printStackTrace();
		}
		
		Response response = new Response();
        response.setStatusCode(StatusCode.SUCCESS.getStatusCode());
        response.setBody(reservations);
        System.out.println(response.responseBuild());
        return response.responseBuild();
	}
}
