package springdb.springtransaction.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest
public class InitTxTest {

    @Autowired Hello hello;

    @Test
    void init(){
        // 초기화 코드 init1()이 실행된다.
        // 모든 빈, AOP 등록 후 init2()이 실행된다.
    }

    @TestConfiguration
    static class InitConfig{
        @Bean
        Hello hello(){
            return new Hello();
        }
    }

    static class Hello{
        @PostConstruct
        @Transactional
        public void init1(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("init1 txActive = {}", txActive);
        }

        @EventListener(ApplicationReadyEvent.class)
        public void init2(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("init2 txActive = {}", txActive);
        }
    }
}
