package com.inho.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {

    @Test
    void checked_catch()
    {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw() throws MyUncheckedException {
        Service service = new Service();
        Assertions.assertThatThrownBy( ()->service.callThrows() )
                .isInstanceOf(MyUncheckedException.class );
    }

    /**
     * RuntimeException을 상속받은 예외는 언체크 예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException
    {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * Unchecked 예외는
     * 에러를 잡아서 처리하거나 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던진다.
     */
    static class Service{
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 된다.
         */
        public void callCatch(){
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                // 예외처리 로직
                log.info("예외처리 ,message={}", e.getMessage(), e);
            }
        }

        public void callThrows()  throws MyUncheckedException {
            repository.call();
        }
    }

    static class Repository
    {
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }

}
