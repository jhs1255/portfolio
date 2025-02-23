package client.container;

import client.controller.MainController;

public class Container {
	private static MainController mainController;

    //컨테이너의 컨트롤러의 정보를 가져오는 걸로하기...
    public static MainController setMainController(){
        //현재 메인컨트롤러 값이 비워져 있다면..
        try {
            if(mainController == null){
                mainController = new MainController();
            }
            return mainController;
        }catch (Exception e){
            //오류 발생시...
            e.printStackTrace();
        }
        return null;
    }
}
