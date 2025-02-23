package dto;

public class MovieData {
	private Integer selectedPlay_info_id;//선택된 영화의 아이디값을 저장할 변수
	private String selectedMovieTitle;  // 선택된 영화의 제목을 저장할 변수
    private String selectedMovieRating;// 선택된 영화의 상영등급을 저장할 변수
    private String selectedMoviePoster;//선텍된 영화의 포스터 정보를 저장할 변수
    private String selectedMovieRuntime;//선택된 영화의 총 상영시간을 저장할 변수
    private Integer selectedMovieSeat;//선택한 상영관의 좌석 수를 저장할 변수
    private String selectedMovieType; // 선택한 영화의 상영관 정보를 저장할 변수
    private String selectedMovieSection; //선택한 영화의 상영관 위치정보를 저장할 변수
    private String selectedMovieDate;// 선택한 영화의 상영날짜를 저장할 변수
    private String selectedMovieStartTime;//선택한 영화의 상영시작시간을 저장할 변수
    private String selectedMovieEndTime;//선택한 영화의 상영종료시간을 저장할 변수
    private Integer discount;
    private Integer Saving;
    private String Phone;
    
    public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Integer getSaving() {
		return Saving;
	}
	public void setSaving(Integer saving) {
		Saving = saving;
	}
	public Integer getSelectedPlay_info_id() {
		return selectedPlay_info_id;
	}
	public void setSelectedPlay_info_id(Integer selectedPlay_info_id) {
		this.selectedPlay_info_id = selectedPlay_info_id;
	}
	
	public String getSelectedMovieTitle() {
		return selectedMovieTitle;
	}
	public void setSelectedMovieTitle(String selectedMovieTitle) {
		this.selectedMovieTitle = selectedMovieTitle;
	}
	
	public String getSelectedMovieRating() {
		return selectedMovieRating;
	}
	public void setSelectedMovieRating(String selectedMovieRating) {
		this.selectedMovieRating = selectedMovieRating;
	}
	public String getSelectedMoviePoster() {
		return selectedMoviePoster;
	}
	public void setSelectedMoviePoster(String selectedMoviePoster) {
		this.selectedMoviePoster = selectedMoviePoster;
	}
	public String getSelectedMovieRuntime() {
		return selectedMovieRuntime;
	}
	public void setSelectedMovieRuntime(String selectedMovieRuntime) {
		this.selectedMovieRuntime = selectedMovieRuntime;
	}
	public Integer getSelectedMovieSeat() {
		return selectedMovieSeat;
	}
	public void setSelectedMovieSeat(Integer selectedMovieSeat) {
		this.selectedMovieSeat = selectedMovieSeat;
	}
	public String getSelectedMovieType() {
		return selectedMovieType;
	}
	public void setSelectedMovieType(String selectedMovieType) {
		this.selectedMovieType = selectedMovieType;
	}
	public String getSelectedMovieSection() {
		return selectedMovieSection;
	}
	public void setSelectedMovieSection(String selectedMovieSection) {
		this.selectedMovieSection = selectedMovieSection;
	}
	public String getSelectedMovieDate() {
		return selectedMovieDate;
	}
	public void setSelectedMovieDate(String selectedMovieDate) {
		this.selectedMovieDate = selectedMovieDate;
	}
	public String getSelectedMovieStartTime() {
		return selectedMovieStartTime;
	}
	public void setSelectedMovieStartTime(String selectedMovieStartTime) {
		this.selectedMovieStartTime = selectedMovieStartTime;
	}
	public String getSelectedMovieEndTime() {
		return selectedMovieEndTime;
	}
	public void setSelectedMovieEndTime(String selectedMovieEndTime) {
		this.selectedMovieEndTime = selectedMovieEndTime;
	}
}
