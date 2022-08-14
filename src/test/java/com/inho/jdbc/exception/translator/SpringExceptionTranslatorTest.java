package com.inho.jdbc.exception.translator;

import com.inho.jdbc.connection.ConnectionConst;
import com.inho.jdbc.domain.Member;
import com.inho.jdbc.repository.ex.MyDbException;
import com.inho.jdbc.repository.ex.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.inho.jdbc.connection.ConnectionConst.PASSWORD;
import static com.inho.jdbc.connection.ConnectionConst.USERID;

@Slf4j
public class SpringExceptionTranslatorTest {

    DriverManagerDataSource dataSource;

    @BeforeEach
    void init()
    {
        dataSource = new DriverManagerDataSource(ConnectionConst.URL, USERID, PASSWORD);
    }

    @Test
    void sqlExceptionErrorCode()
    {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "select bad grammer";

        try{
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
        }catch (SQLException e)
        {
            int erroCode =e.getErrorCode();
            Assertions.assertThat(erroCode).isEqualTo(42122);

        }finally {

        }
    }


    @Test
    void exceptionTranslator()
    {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "select bad grammer";

        try{
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
        }catch (SQLException e)
        {
            Assertions.assertThat(e.getErrorCode()).isEqualTo(42122);
            SQLErrorCodeSQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            DataAccessException resultEx = exTranslator.translate("select", sql, e);
            log.info("resultEx", resultEx);

            Assertions.assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }finally {

        }
    }

}
