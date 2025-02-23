package dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {
	private RequestCode requestCode;

    private Object body;

    public String requestBuild(){
        String returnValue = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            returnValue = objectMapper.writeValueAsString(this);
        }
        catch (Exception e){
            e.printStackTrace();
            return "{}"; // 직렬화 실패 시 빈 JSON 객체 반환
        }
        System.out.println(returnValue);
        return returnValue;
    }

    public RequestDto jsonStringToObject(String string){
        ObjectMapper objectMapper = new ObjectMapper();
        RequestDto requestDto;
        try {
            requestDto = objectMapper.readValue(string, RequestDto.class);
            return requestDto;

        }catch (Exception e){
        	e.printStackTrace();
        }
        return null;
    }

	public RequestCode getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(RequestCode requestCode) {
		this.requestCode = requestCode;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
