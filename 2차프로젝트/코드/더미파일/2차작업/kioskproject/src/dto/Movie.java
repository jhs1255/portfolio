package dto;

import java.util.ArrayList;


public class Movie {
	private Integer res_id;//예약 정보를 넘겨주기 위해 가져옴
	private String title; //영화 제목
	private String rating; //관람 등급
	private String runtime;//상영시간
	private String poster;//영화사진
	private String kind; //'theater'테이블에서 가져온 상영관 타입 - 영화 종류와 상영관의 종류에 따라 값을 묶기 위해 가져옴
	private ArrayList<Screening> screenings;
	
	public Movie(Integer play_Info_id, String title, String rating, String runtime, String poster, String kind) {
		this.res_id = play_Info_id;
		this.title = title;
		this.rating = rating;
		this.runtime = runtime;
		this.poster = poster;
		this.kind = kind;
		this.screenings = new ArrayList<>();
	}
	public ArrayList<Screening> getScreenings() {
		return screenings;
	}
	public void setScreenings(ArrayList<Screening> screenings) {
		this.screenings = screenings;
	}
	public void addScreening(String date, String startTime, String endTime,Integer seat, String section) {
        screenings.add(new Screening(date, startTime, endTime, seat, section));
    }

	public Integer getRes_id() {
		return res_id;
	}
	public void setRes_id(Integer res_id) {
		this.res_id = res_id;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
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

	public static class Screening {
		private String section;// 영화 상영관의 위치를 가져옴
		private Integer seat;//영화관의 좌석 수를 가져옴
		private String date;//문자열로 변환된 상영날짜를 가져옴
		private String startTime; // 상영시작 시간을 가져옴
		private String endTime; // 상영종료시간을 가져옴
		
		public Screening(String date, String startTime, String endTime, Integer seat, String section) {
	        this.date = date;
	        this.startTime = startTime;
	        this.endTime = endTime;
	        this.seat = seat;
	        this.section = section;
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

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
	}
}
