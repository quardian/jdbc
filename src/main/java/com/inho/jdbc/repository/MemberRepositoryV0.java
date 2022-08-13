package com.inho.jdbc.repository;

import com.inho.jdbc.connection.DBConnectionUtil;
import com.inho.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into Member(member_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try
        {
            con   = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, member.getMemberId() );
            pstmt.setInt(2, member.getMoney() );

            int afftects = pstmt.executeUpdate();

            return member;
        }
        catch (Exception e)
        {
            log.error("db error", e);
            throw e;
        }
        finally {
            close(con, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            con   = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, memberId );

            rs = pstmt.executeQuery();

            if ( rs.next() ){
                Member member = new Member(rs.getString("member_id"), rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        }
        catch (Exception e)
        {
            log.error("db error", e);
            throw e;
        }
        finally {
            close(con, pstmt, rs);
        }
    }

    public int update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try
        {
            con   = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, money  );
            pstmt.setString(2, memberId);

            return pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            log.error("db error", e);
            throw e;
        }
        finally {
            close(con, pstmt, null);
        }
    }

    public int delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            con   = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, memberId );

            return pstmt.executeUpdate();

        }
        catch (Exception e)
        {
            log.error("db error", e);
            throw e;
        }
        finally {
            close(con, pstmt, rs);
        }
    }



    private void close(Connection con, Statement stmt, ResultSet rs)
    {
        try{
            if ( rs != null ) rs.close();
        }
        catch (Exception e){
            log.info("error", e);
        }

        try{
            if ( stmt != null ) stmt.close();
        }
        catch (Exception e){
            log.info("error", e);
        }

        try{
            if ( con != null ) con.close();
        }
        catch (Exception e){
            log.info("error", e);
        }
    }

}
