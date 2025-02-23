package client.dto;

public class MemberDto {
	private String phoneNumber;
    //적립금액
    private int saving;
    //사용한 할인 금액
    private int discount;
  //사용가능한 포인트
  	private Integer availablePoint;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getSaving() {
		return saving;
	}
	public void setSaving(int saving) {
		this.saving = saving;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public Integer getAvailablePoint() {
		return availablePoint;
	}
	public void setAvailablePoint(Integer availablePoint) {
		this.availablePoint = availablePoint;
	}
	
	public MemberDto(Integer available) {
		this.availablePoint = available;
	}
}
