package classLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final String url = "jdbc:mysql://10.2.16.14:3306/kiosk";
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        url,
                        "root",
                        "root1234"
                );
                System.out.println("연결에 성공하였습니다");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return connection;
        }
    }
}
