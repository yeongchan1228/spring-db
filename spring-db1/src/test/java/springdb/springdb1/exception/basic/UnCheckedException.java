package springdb.springdb1.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedException {

    @Test
    void catchCheck(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void ThrowCheck(){
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyUnCheckedException.class);
    }

    static class MyUnCheckedException extends RuntimeException{
        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    static class Service{
        Repository repository = new Repository();

        void callCatch(){
            try {
                repository.call();
            }catch (MyUnCheckedException e){
                log.info("예외 처리, Message = {}", e.getMessage(), e);
            }
        }

        void callThrow(){
            repository.call();
        }
    }

    static class Repository{
        void call(){
            throw new MyUnCheckedException("ex");
        }
    }
}
