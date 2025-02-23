package dto;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseDto {
	private int statusCode;

    private Object body;

    public String responseBuild(){
        String returnValue = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            returnValue = objectMapper.writeValueAsString(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(returnValue);
        return returnValue;
    }
    public Map<String, Object> jsonStringToObject(String string){
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
        	Map<String, Object> jsonMap = objectMapper.readValue(string, new TypeReference<Map<String, Object>>() {});
        	return jsonMap;

        }catch (Exception e){

        }
        return null;
    }
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
}
