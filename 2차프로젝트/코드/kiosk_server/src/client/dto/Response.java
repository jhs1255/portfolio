package client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {
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
