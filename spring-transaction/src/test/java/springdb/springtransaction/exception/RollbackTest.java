package springdb.springtransaction.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class RollbackTest {

    @Autowired RollbackService rollbackService;

    @Test
    void runtimeException() {
        assertThatThrownBy(() -> rollbackService.runtimeException()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException() {
        assertThatThrownBy(() -> rollbackService.checkedException()).isInstanceOf(MyException.class);
    }

    @Test
    void rollbackFor() {
        assertThatThrownBy(() -> rollbackService.checkedRollbackException()).isInstanceOf(MyException.class);
    }


    @TestConfiguration
    static class RollbackTestConfig {
        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    static class RollbackService {
        // 런타임 예외 발생 : 롤백
        @Transactional
        public void runtimeException() {
            log.info("Call RuntimeException");
            throw new RuntimeException();
        }

        // 체크 예외 발생 : 커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("Call CheckedException");
            throw new MyException();
        }

        // 체크 예외 발생 rollbackFor 지정 : 롤백
        @Transactional(rollbackFor = MyException.class)
        public void checkedRollbackException() throws MyException {
            log.info("Call RollbackFor");
            throw new MyException();
        }
    }

    static class MyException extends Exception {
        public MyException() {
        }

        public MyException(String message) {
            super(message);
        }

        public MyException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
