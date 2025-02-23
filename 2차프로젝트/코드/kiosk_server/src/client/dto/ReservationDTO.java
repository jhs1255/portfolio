package client.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReservationDTO {
	private String phoneNumber;
	  //적립여부
	    private Integer isSave;
	    //사용할 포인트 금액
	    private Integer discount = 0;
	    private Integer savePoint = 0;
		private Integer play_info_id;
		private Integer res_id;
		private String movieTitle;
		private String movieType;
		private String movieRating;
		private String movieRuntime;
		private String movieDate;
		private String movieStart;
		private String movieEnd;
		private String movieTheater;
		private String Seats;
		private String people;
		private String randomNumber;
		
		private String status;
		
		private MovieData movieData; // 영화 데이터
	    private String seats; // 선택된 좌석 정보
	    private int totalPrice; // 총 가격
	    private String reservationNumber; // 예약 번호
	    private List<String> peopleDetails; // 인원 정보
	    
		
		
		
		public ReservationDTO(Integer Res_id, String movie_title, String Rating, String movietype, String Runtime,
				String localDate, String start, String end, String theater, String Seats,
				String People, String ResNum, String Status) {
			this.res_id = Res_id;
			this.movieTitle = movie_title;
			this.movieRating = Rating;
			this.movieType = movietype;
			this.movieRuntime = Runtime;
			this.movieDate = localDate;
			this.movieStart = start;
			this.movieEnd = end;
			this.movieTheater = theater;
			this.Seats = Seats;
			this.people = People;
			this.randomNumber = ResNum;
			this.status = Status;
		}
		
		
		




		public Integer getSavePoint() {
			return savePoint;
		}


		public void setSavePoint(Integer savePoint) {
			this.savePoint = savePoint;
		}


		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Integer getRes_id() {
			return res_id;
		}
		public void setRes_id(Integer res_id) {
			this.res_id = res_id;
		}
		public Integer getIsSave() {
			return isSave;
		}
		public void setIsSave(Integer isSave) {
			this.isSave = isSave;
		}
		public MovieData getMovieData() {
			return movieData;
		}
		public void setMovieData(MovieData movieData) {
			this.movieData = movieData;
		}
		public int getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(int totalPrice) {
			this.totalPrice = totalPrice;
		}
		public String getReservationNumber() {
			return reservationNumber;
		}
		public void setReservationNumber(String reservationNumber) {
			this.reservationNumber = reservationNumber;
		}
		public List<String> getPeopleDetails() {
			return peopleDetails;
		}
		public void setPeopleDetails(List<String> peopleDetails) {
			this.peopleDetails = peopleDetails;
		}
		public void setDiscount(Integer discount) {
			this.discount = discount;
		}

		public Integer getPlay_info_id() {
			return play_info_id;
		}
		public void setPlay_info_id(Integer play_info_id) {
			this.play_info_id = play_info_id;
		}
		public String getMovieTitle() {
			return movieTitle;
		}
		public void setMovieTitle(String movieTitle) {
			this.movieTitle = movieTitle;
		}
		public String getMovieType() {
			return movieType;
		}
		public void setMovieType(String movieType) {
			this.movieType = movieType;
		}
		
		public String getMovieRating() {
			return movieRating;
		}
		public void setMovieRating(String movieRating) {
			this.movieRating = movieRating;
		}
		public String getMovieRuntime() {
			return movieRuntime;
		}
		public void setMovieRuntime(String movieRuntime) {
			this.movieRuntime = movieRuntime;
		}

		public String getMovieDate() {
			return movieDate;
		}
		public void setMovieDate(String movieDate) {
			this.movieDate = movieDate;
		}
		public String getMovieStart() {
			return movieStart;
		}
		public void setMovieStart(String movieStart) {
			this.movieStart = movieStart;
		}
		public String getMovieEnd() {
			return movieEnd;
		}
		public void setMovieEnd(String movieEnd) {
			this.movieEnd = movieEnd;
		}
		public String getMovieTheater() {
			return movieTheater;
		}
		public void setMovieTheater(String movieTheater) {
			this.movieTheater = movieTheater;
		}
		public String getSeats() {
			return Seats;
		}
		public void setSeats(String seats) {
			Seats = seats;
		}
		public String getPeople() {
			return people;
		}
		public void setPeople(String people) {
			this.people = people;
		}
		public String getRandomNumber() {
			return randomNumber;
		}
		public void setRandomNumber(String randomNumber) {
			this.randomNumber = randomNumber;
		}
		
		// 좌석 정보를 리스트로 변환하는 메서드
	    public List<String> getSeatList() {
	        return seats != null ? Arrays.asList(seats.split(",")) : Collections.emptyList();
	    }

	    // 인원 정보를 리스트로 변환하는 메서드
	    public List<String> getPeopleList() {
	        return people != null ? Arrays.asList(people.split(",")) : Collections.emptyList();
	    }	
	    
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public Integer isSave() {
			return isSave;
		}
		public void setSave(Integer isSave) {
			this.isSave = isSave;
		}
		public int getDiscount() {
			return discount;
		}
		public void setDiscount(int discount) {
			this.discount = discount;
		}
}
