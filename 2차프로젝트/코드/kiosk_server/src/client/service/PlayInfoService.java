package client.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import classLoader.Connect;
import client.dto.PlayInfoDto;
import client.dto.Response;
import enumcode.StatusCode;

public class PlayInfoService {
	//데이테베이스에 접근하기 위한 세팅값을 가져옵니다..
    Connection connection = Connect.getConnection();
    
    public String getPlayInfo(LocalDateTime date) throws JsonProcessingException{
    	System.out.println("값을 가져오는 sql을 실행합니다...");
    	if(date == null){
            //인자로 받은 값이 null값이라면....
            date = LocalDateTime.now();
        }
    	String sql = "SELECT sm.title, sm.runtime, sm.rating, sm.poster, sm.movietype, t.kind, t.section,t.seat,p.play_info_id, p.movie_date, p.start_time, p.end_time "+
				 "FROM play_info p "+
				 "JOIN showmovie sm ON p.movie_id = sm.movie_id "+
				 "JOIN theater t ON p.theater_id = t.theater_id "+
				 "where sm.movietype = t.kind AND p.movie_date = ? " +
				 "ORDER BY sm.title, t.kind, p.movie_date";
    	List<PlayInfoDto> playInfoDtoList = new ArrayList<>();
    	try {
    		PreparedStatement pstmt = connection.prepareStatement(sql);
    		pstmt.setDate(1, Date.valueOf(date.toLocalDate()));
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    			PlayInfoDto playInfoDto = new PlayInfoDto(
        				rs.getInt("play_info_id"),
        				rs.getString("title"),
        				rs.getString("runtime"),
        				rs.getString("rating"),
        				rs.getString("poster"),
        				rs.getString("kind"),
        				rs.getString("section"),
        				rs.getInt("seat"),
        				rs.getDate("movie_date").toLocalDate().toString(),
        				rs.getTime("start_time").toLocalTime().toString(),
        				rs.getTime("end_time").toLocalTime().toString(),
        				new ArrayList<String>()
        				);
    			playInfoDtoList.add(playInfoDto); // 리스트에 추가
    		}
    		
    	}catch(Exception e) {
    		System.out.println("오류발생..");
    		e.printStackTrace();
    	}
    	//만약 오류가 없이 완벽하게 값이 다 넣어졌다면...
        Response response = new Response();
        response.setStatusCode(StatusCode.SUCCESS.getStatusCode());
        response.setBody(playInfoDtoList);
        System.out.println(response.responseBuild());
        return response.responseBuild();
    }

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
    
}
