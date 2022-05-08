package springdb.springdb1.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static springdb.springdb1.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("get connection = {}, class = {}", connection, connection.getClass());
        return connection;
    }
}