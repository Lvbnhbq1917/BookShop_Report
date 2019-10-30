import java.sql.*;

public class ConnectionToBD {
    private static volatile Connection con;

    private ConnectionToBD() {}

    public static Connection getConnectionToBD() throws SQLException, ClassNotFoundException {
        if (con == null) {
            synchronized (Connection.class) {
                if (con == null) {
                    Class.forName("org.postgresql.Driver");
                    con = DriverManager.getConnection(
                            Config.getProperty(Config.DB_URL),
                            Config.getProperty(Config.DB_LOGIN),
                            Config.getProperty(Config.DB_PASSWORD)
                    );
                }
            }
        }
        return con;
    }
}
