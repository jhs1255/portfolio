package server.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import classLoader.Connect;
import client.dto.Response;
import enumcode.StatusCode;
import security.Encrypt;

public class LoginService {
	public String login(String id, String password){
        Connection connection = Connect.getConnection();
        String getMemberInfo = "select * from admin where admin_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getMemberInfo);

            Encrypt encrypt = new Encrypt();
            password = encrypt.encrypt(password);
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            //만약 값이 존재한다면...
            if(rs.next()){
                System.out.println("id가 존재합니다...");
                String passwordDataBase = rs.getString("admin_password");
                if(passwordDataBase.equals(password)){
                    Response response = new Response();
                    response.setStatusCode(StatusCode.SUCCESS.getStatusCode());
                    return response.responseBuild();
                }
                else{
                    Response response = new Response();
                    response.setStatusCode(StatusCode.NOT_FOUND.getStatusCode());
                    return response.responseBuild();
                }
            }
            else {
                Response response = new Response();
                response.setStatusCode(StatusCode.NOT_FOUND.getStatusCode());
                return response.responseBuild();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
