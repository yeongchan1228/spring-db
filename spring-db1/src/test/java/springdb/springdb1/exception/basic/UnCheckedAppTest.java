package springdb.springdb1.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
public class UnCheckedAppTest {

    @Test
    void test(){
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void printEx(){
        Controller controller = new Controller();
        try {
            controller.request();
        }catch (Exception e){
            log.info("exception", e);
        }
    }

    static class Controller{
         Service service = new Service();

         void request() {
             service.logic();
         }
    }

    static class Service{
        NetworkClient networkClient = new NetworkClient();
        Repository repository = new Repository();

        void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient{
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository{
        public void call() {
            try {
                runSQL();
            }catch (SQLException e){
                throw new RuntimeSQLException(e);
            }
        }

        void runSQL() throws SQLException {
            throw new SQLException("SQL 예외");
        }
    }

    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException() {
            super();
        }

        public RuntimeConnectException(String message) {
            super(message);
        }

        public RuntimeConnectException(String message, Throwable cause) {
            super(message, cause);
        }

        public RuntimeConnectException(Throwable cause) {
            super(cause);
        }
    }

    static class RuntimeSQLException extends RuntimeException{
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
