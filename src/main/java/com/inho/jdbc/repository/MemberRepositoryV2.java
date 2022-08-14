package com.inho.jdbc.repository;

import com.inho.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV2 {

    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into Member(member_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try
        {
            con   = getConnection();
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
            con   = getConnection();
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

    public Member findById(Connection con, String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
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
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass() );
        return con;
        //return DBConnectionUtil.getConnection();
    }

    public int update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try
        {
            con   = getConnection();
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


    public int update(Connection con, String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        PreparedStatement pstmt = null;

        try
        {
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
            JdbcUtils.closeStatement(pstmt);
        }
    }



    public int delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            con   = getConnection();
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
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

}
