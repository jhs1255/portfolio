package dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class ReservationDTO {
	private String movieTitle;
	private String movieType;
	private String movieRating;
	private String movieRuntime;
	private LocalDate movieDate;
	private LocalTime movieStart;
	private LocalTime movieEnd;
	private String movieTheater;
	private String Seats;
	private String people;
	private String randomNumber;
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
	public LocalDate getMovieDate() {
		return movieDate;
	}
	public void setMovieDate(LocalDate movieDate) {
		this.movieDate = movieDate;
	}
	public LocalTime getMovieStart() {
		return movieStart;
	}
	public void setMovieStart(LocalTime movieStart) {
		this.movieStart = movieStart;
	}
	public LocalTime getMovieEnd() {
		return movieEnd;
	}
	public void setMovieEnd(LocalTime movieEnd) {
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
        return Arrays.asList(Seats.split(","));
    }

    // 인원 정보를 리스트로 변환하는 메서드
    public List<String> getPeopleList() {
        return Arrays.asList(people.split(","));
    }
}
