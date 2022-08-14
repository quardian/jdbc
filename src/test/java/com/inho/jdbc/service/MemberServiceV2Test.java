package com.inho.jdbc.service;

import com.inho.jdbc.domain.Member;
import com.inho.jdbc.repository.MemberRepositoryV1;
import com.inho.jdbc.repository.MemberRepositoryV2;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.inho.jdbc.connection.ConnectionConst.*;

@Slf4j
class MemberServiceV2Test {
    public static final String MEMBER_A  = "memberA";
    public static final String MEMBER_B  = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberServiceV2 memberService;
    private MemberRepositoryV2 memberRepository;

    @BeforeEach
    void init() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERID);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        memberRepository = new MemberRepositoryV2(dataSource);
        memberService = new MemberServiceV2(dataSource, memberRepository);

        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }
    @AfterEach
    void destory() throws SQLException {

    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        memberService.accountTransfer(MEMBER_A, MEMBER_B, 2000);

        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());

        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("예외이체")
    void accountTransferException() throws SQLException {
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000);

        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberEx = memberRepository.findById(memberEx.getMemberId());

        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(10000);
        Assertions.assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }
}