package dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationDAO {
	
	//예매한 좌석 확인 메소드
	public Set<String> getReservedSeats(String movieName, String movieDate, String movieStart) {
        Set<String> reservedSeats = new HashSet<>();
        
        String query = "SELECT seats FROM reservation WHERE movie_name = ? AND movie_date = ? AND movie_start = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, movieName);
            stmt.setString(2, movieDate);
            stmt.setString(3, movieStart);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String[] seats = rs.getString("seats").split(", ");
                for (String seat : seats) {
                    reservedSeats.add(seat);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return reservedSeats;
    }

	
	//예약 완료 DB로 값 전송
	public void saveReservation(MovieData movieData, String seats, int totalPrice, String reservationNumber, List<String> peopleDetails) {
		String sql = "INSERT INTO reservation (movie_name, movie_type,rating, movie_runtime, movie_date, movie_start, movie_end, movie_theater, seats,  people, resNumber, totalprice) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try(Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
			// 각 필드에 값 설정
	        pstmt.setString(1, movieData.getSelectedMovieTitle());
	        pstmt.setString(2, movieData.getSelectedMovieType());
	        pstmt.setString(3, movieData.getSelectedMovieRating());
	        pstmt.setString(4, movieData.getSelectedMovieRuntime());
	        pstmt.setString(5, movieData.getSelectedMovieDate());
	        pstmt.setString(6, movieData.getSelectedMovieStartTime());
	        pstmt.setString(7, movieData.getSelectedMovieEndTime());
	        pstmt.setString(8, movieData.getSelectedMovieSection());
	        pstmt.setString(9, seats); // 선택한 좌석 정보
	        pstmt.setString(10, String.join(", ", peopleDetails)); // 인원 정보
	        pstmt.setString(11, reservationNumber); // 예매 번호
	        pstmt.setInt(12, totalPrice);
	        /*pstmt.setInt(12, usePoint);
	        pstmt.setInt(13, savePoint);*/
	     // 쿼리 실행
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("예약 정보가 성공적으로 저장되었습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	// 예매 번호가 DB에 있는지 확인하는 메서드
	public boolean isReservationNumberValid(String reservationNumber) {
		String sql = "SELECT COUNT(*) FROM reservation where resNumber = ?";
		try (Connection con = DBConnection.getConnection();
	             PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, reservationNumber);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;  // 예매 번호가 있으면 true 반환
            }
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	 // 예매 번호로 정보를 가져오는 메서드
    public ReservationDTO getReservationByNumber(String reservationNumber) {
    	String sql = "select movie_name, movie_type,rating ,movie_runtime, movie_date, movie_start, movie_end, movie_theater, seats, people, resNumber FROM reservation where resNumber = ?";
    	ReservationDTO reservation = null;
    	
    	try (Connection con = DBConnection.getConnection();
	             PreparedStatement pstmt = con.prepareStatement(sql)){
    		pstmt.setString(1, reservationNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()) {
            	reservation = new ReservationDTO();
            	reservation.setMovieTitle(rs.getString("movie_name"));
            	reservation.setMovieType(rs.getString("movie_type"));
            	reservation.setMovieRating(rs.getString("rating"));
            	reservation.setMovieRuntime(rs.getString("movie_runtime"));
            	reservation.setMovieDate(rs.getDate("movie_date").toLocalDate());
            	reservation.setMovieStart(rs.getTime("movie_start").toLocalTime());
            	reservation.setMovieEnd(rs.getTime("movie_end").toLocalTime());
            	reservation.setMovieTheater(rs.getString("movie_theater"));
            	reservation.setSeats(rs.getString("seats"));
            	reservation.setPeople(rs.getString("people"));
            	reservation.setRandomNumber(rs.getString("resNumber"));
            }
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
		return reservation;
    }
    
    
    public List<ReservationDTO> getReservationsByPhoneNumber(String phoneNumber) {
        List<ReservationDTO> reservations = new ArrayList<>();
        String query = "SELECT " +
                       "r.resNumber, " +
                       "r.movie_name, " +
                       "r.rating, " +
                       "r.movie_type, " +
                       "r.movie_runtime, " +
                       "r.movie_date, " +
                       "r.movie_start, " +
                       "r.movie_end, "+
                       "r.movie_theater, " +
                       "r.seats, " +
                       "r.people " +
                       "FROM user u " +
                       "JOIN reservation r ON u.user_id = r.user_id " +
                       "WHERE u.phone = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ReservationDTO reservation = new ReservationDTO();
                reservation.setRandomNumber(rs.getString("resNumber"));
                reservation.setMovieTitle(rs.getString("movie_name"));
            	reservation.setMovieType(rs.getString("movie_type"));
            	reservation.setMovieRating(rs.getString("rating"));
            	reservation.setMovieRuntime(rs.getString("movie_runtime"));
            	reservation.setMovieDate(rs.getDate("movie_date").toLocalDate());
            	reservation.setMovieStart(rs.getTime("movie_start").toLocalTime());
            	reservation.setMovieEnd(rs.getTime("movie_end").toLocalTime());
            	reservation.setMovieTheater(rs.getString("movie_theater"));
            	reservation.setSeats(rs.getString("seats"));
            	reservation.setPeople(rs.getString("people"));

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }
}
