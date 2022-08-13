package com.inho.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.inho.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection()
    {
        try {
            Connection connection = DriverManager.getConnection(URL, USERID, PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass() );
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
