package com.inho.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckTest {

    @Test
    void checked_catch()
    {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw() throws MyCheckedException {
        Service service = new Service();
        Assertions.assertThatThrownBy( ()->service.callThrows() )
                .isInstanceOf(MyCheckedException.class );
    }

    /**
     * Exception을 상속받은 예외는 체크 예외가 된다.
     */
    static class MyCheckedException extends Exception
    {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    /**
     * Checked 예외는
     * 에러를 잡아서 처리하거나 던저야 한다.
     */
    static class Service{
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch(){
            try {
                repository.call();
            } catch (MyCheckedException e) {
                // 예외처리 로직
                log.info("예외처리 ,message={}", e.getMessage(), e);
            }
        }

        public void callThrows() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository
    {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }

}
