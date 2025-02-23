package server.controller;

import server.service.LoginService;

public class MainController {
	public LoginService getLoginService() {
		return loginService;
	}

	private final LoginService loginService = new LoginService();
	
	public String login(String id, String password){
        String answer = loginService.login(id, password);
        System.out.println(answer);
        return answer;
    }
}
