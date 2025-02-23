package dto;

import java.util.ArrayList;

public class PlayInfoDto {
	//상영 시간 ID
		private Integer play_info_id;
		//영화 제목
		private String title;
		//상영시간
		private String runtime;
		//관람 연령 등급
		private String rating;
		//포스터
		private String poster;
		//상영관 종류
		private String kind;
		//상영관 위치
		private String section;
		//총 좌석 수
		private Integer seat;
		//상영 날짜
		private String movieDate;
		//상영 시작 시간
		private String startTimes;
		//상영 종료 시간
		private String endTime;
		
		//이미 예약된 좌석들...
	    private ArrayList<String> reserveSeats;

		public Integer getPlay_info_id() {
			return play_info_id;
		}

		public void setPlay_info_id(Integer play_info_id) {
			this.play_info_id = play_info_id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRuntime() {
			return runtime;
		}

		public void setRuntime(String runtime) {
			this.runtime = runtime;
		}

		public String getRating() {
			return rating;
		}

		public void setRating(String rating) {
			this.rating = rating;
		}

		public String getPoster() {
			return poster;
		}

		public void setPoster(String poster) {
			this.poster = poster;
		}

		public String getKind() {
			return kind;
		}

		public void setKind(String kind) {
			this.kind = kind;
		}

		public String getSection() {
			return section;
		}

		public void setSection(String section) {
			this.section = section;
		}

		public Integer getSeat() {
			return seat;
		}

		public void setSeat(Integer seat) {
			this.seat = seat;
		}

		public String getMovieDate() {
			return movieDate;
		}

		public void setMovieDate(String movieDate) {
			this.movieDate = movieDate;
		}

		public String getStartTimes() {
			return startTimes;
		}

		public void setStartTimes(String startTimes) {
			this.startTimes = startTimes;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public ArrayList<String> getReserveSeats() {
			return reserveSeats;
		}

		public void setReserveSeats(ArrayList<String> reserveSeats) {
			this.reserveSeats = reserveSeats;
		}
	    
}
