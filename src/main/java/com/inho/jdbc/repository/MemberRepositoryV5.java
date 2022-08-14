package com.inho.jdbc.repository;

import com.inho.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JdbcTemplate 사용
 *
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{
    private final JdbcTemplate jdbcTemplate;


    public MemberRepositoryV5(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    /**
     *
     * @param member
     * @return
     * @throws SQLException
     */
    @Override
    public Member save(Member member)  {
        String sql = "insert into Member(member_id, money) values(?,?)";
        jdbcTemplate.update(sql, member.getMemberId(), member.getMoney() );
        return member;
    }

    /**
     *
     * @param memberId
     * @return
     * @throws SQLException
     */
    @Override
    public Member findById(String memberId)  {
        String sql = "select * from member where member_id = ?";
        return jdbcTemplate.queryForObject(sql, getMemberRowMapper(), memberId);
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (rs, rowNum)->{
            Member member = new Member();
            member.setMemberId( rs.getString("member_id"));
            member.setMoney( rs.getInt("money"));
            return member;
        };
    }


    /**
     *
     * @param memberId
     * @param money
     * @return
     * @throws SQLException
     */
    @Override
    public int update(String memberId, int money)  {
        String sql = "update member set money=? where member_id=?";
        return jdbcTemplate.update(sql, money, memberId);
    }


    /**
     *
     * @param memberId
     * @return
     * @throws SQLException
     */
    @Override
    public int delete(String memberId)  {
        String sql = "delete from member where member_id = ?";
        return jdbcTemplate.update(sql, memberId);
    }



}
