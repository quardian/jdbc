package com.inho.jdbc.service;

import com.inho.jdbc.domain.Member;
import com.inho.jdbc.repository.MemberRepositoryV1;
import com.inho.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);

            bizLogic(con, fromId, toId, money);

            con.commit();
        }
        catch (Exception e)
        {
            con.rollback();
        }
        finally
        {
            releaseConnection(con);
        }

    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember   = memberRepository.findById(con, fromId);
        Member toMember     = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId,   fromMember.getMoney() + money);
    }

    private void releaseConnection(Connection con) {
        if ( con != null ){
            try{
                con.setAutoCommit(true);
                con.close();
            }catch (Exception e){
                log.info("error", e);
            }
        }
    }

    private void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외발생");
        }
    }

}
