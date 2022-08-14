package com.inho.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class UncheckedAppTest {

    @Test
    void unchecked()
    {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy( ()-> controller.request() )
                .isInstanceOf(Exception.class);
    }


    @Test
    void printEx()
    {
        Controller controller = new Controller();
        try{
            controller.request();
        }catch (Exception e){
            log.info("ex", e);
        }
    }

    static class Controller {
        Service service = new Service();
        public void request() {
            service.logic();
        }
    }

    /**
     * Checked 예외는
     * 에러를 잡아서 처리하거나 던저야 한다.
     */
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic()  {
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient {
        public void call() throws RuntimeConnectException {
            throw new RuntimeConnectException("연결실패");
        }
    }

    static class Repository
    {
        public void call() throws RuntimeSQLException {
            try{
                runSQL();
            }
            catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }


    static class RuntimeConnectException extends RuntimeException
    {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException
    {
        public RuntimeSQLException(String message) {
            super(message);
        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
