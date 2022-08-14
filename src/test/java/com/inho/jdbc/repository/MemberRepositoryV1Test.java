package com.inho.jdbc.repository;

import com.inho.jdbc.connection.ConnectionConst;
import com.inho.jdbc.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.inho.jdbc.connection.ConnectionConst.*;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach()
    {
        // 기본 DriverManager - 항상 새로운 커넥션을 획득
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERID, PASSWORD);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERID);
        dataSource.setPassword(PASSWORD);

        repository = new MemberRepositoryV1(dataSource);



    }

    @Test
    void crud() throws SQLException
    {
        // 등록(C)
        Member member = new Member("memberV4", 10000);
        repository.save(member);

        // 검색(R)
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        Assertions.assertThat(findMember).isEqualTo(member);

        // 수정(U)
        repository.update(member.getMemberId(), 50000);
        Member updateMember = repository.findById(member.getMemberId());
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(50000);
        
        // 삭제(D)
        repository.delete(member.getMemberId());
        Assertions.assertThatThrownBy( ()-> repository.findById(member.getMemberId()) )
                .isInstanceOf(NoSuchElementException.class);

    }
}