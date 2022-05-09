package springdb.springdb1.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static springdb.springdb1.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    /**
     * Connection Pool 사용 X
     */

    @Test
    public void driverManager() throws Exception {
        // given
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // then
        log.info("connection = {}, class = {}", connection, connection.getClass());
        log.info("connection2 = {}, class = {}", connection2, connection2.getClass());
    }
    
    @Test
    public void driverManagerDataSource() throws Exception {
        // driverManagerDataSource 항상 새로운 커넥 획득
        // given
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // then
        userDataSource(dataSource);
    }

    /**
     * Connection Pool 사용 O
     */
    
    @Test
    public void dataSourceConnectionPool() throws Exception {
        // given
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        // then
        userDataSource(dataSource);
        Thread.sleep(1000);
    }

    private void userDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();

        log.info("con1 = {}, class = {}", con1, con1.getClass());
        log.info("con2 = {}, class = {}", con2, con2.getClass());
    }
}
