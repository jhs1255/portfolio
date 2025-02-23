package dto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public static Connection getConnection() {
		Connection con = null;
		try {
			String url = "jdbc:mysql://localhost:3306/kiosk";
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("데이터베이스 연결중...");
			con = DriverManager.getConnection(url,"root","root1234");
			System.out.println("연결성공");
		}catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
        }
		
		return con;
	}
}
