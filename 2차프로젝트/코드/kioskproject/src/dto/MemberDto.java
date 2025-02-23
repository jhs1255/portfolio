package dto;

public class MemberDto {
	//입력받은 휴대폰 번호
	private String phoneNumber;
	//사용가능한 포인트
	private Integer availablePoint;
	//할인금액
	private Integer discount;
	//적립금액
	private Integer saving;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Integer getAvailablePoint() {
		return availablePoint;
	}
	public void setAvailablePoint(Integer availablePoing) {
		this.availablePoint = availablePoing;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Integer getSaving() {
		return saving;
	}
	public void setSaving(Integer saving) {
		this.saving = saving;
	}
	
	
}
