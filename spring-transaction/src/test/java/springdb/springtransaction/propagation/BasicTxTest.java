package springdb.springtransaction.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired PlatformTransactionManager transactionManager;

    @TestConfiguration
    static class Config{

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit(){
        log.info("Transaction Start");
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionAttribute());

        log.info("Transaction Commit Start");
        transactionManager.commit(status);
        log.info("Transaction Commit Complete");
    }

    @Test
    void rollback(){
        log.info("Transaction Start");
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionAttribute());

        log.info("Transaction Rollback Start");
        transactionManager.rollback(status);
        log.info("Transaction Rollback Complete");
    }

    @Test
    void double_commit(){
        log.info("Transaction1 Start");
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("Transaction1 Commit");
        transactionManager.commit(status);

        log.info("Transaction2 Start");
        TransactionStatus status2 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("Transaction2 Commit");
        transactionManager.commit(status2);
    }

    @Test
    void oneCommitOneRollback(){
        log.info("Transaction1 Start");
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("Transaction1 Commit");
        transactionManager.commit(status);

        log.info("Transaction2 Start");
        TransactionStatus status2 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("Transaction2 Rollback");
        transactionManager.rollback(status2);
    }

    @Test
    void innerCommit() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction = {}", outer.isNewTransaction()); // 처음 수행된 트랜잭션인지

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction = {}", inner.isNewTransaction()); // 처음 수행된 트랜잭션인지
        log.info("내부 트랜잭션 커밋");
        transactionManager.commit(inner);

        log.info("외부 트랜잭션 커밋");
        transactionManager.commit(outer);
    }
}
