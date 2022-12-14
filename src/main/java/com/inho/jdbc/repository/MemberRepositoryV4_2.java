package com.inho.jdbc.repository;

import com.inho.jdbc.domain.Member;
import com.inho.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * SQLExceptionTranslator
 *
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository{

    private final DataSource dataSource;
    private final SQLExceptionTranslator exceptionTranslator;

    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
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
        catch (SQLException e)
        {
            log.error("db error", e);
            throw exceptionTranslator.translate("save", sql, e);
        }
        finally {
            close(con, pstmt, null);
        }
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
        catch (SQLException e)
        {
            log.error("db error", e);
            throw exceptionTranslator.translate("save", sql, e);
        }
        finally {
            close(con, pstmt, rs);
        }
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
        catch (SQLException e)
        {
            log.error("db error", e);
            throw exceptionTranslator.translate("update", sql, e);
        }
        finally {
            close(con, pstmt, null);
        }
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
        catch (SQLException e)
        {
            log.error("db error", e);
            throw exceptionTranslator.translate("update", sql, e);
        }
        finally {
            close(con, pstmt, rs);
        }
    }


    /** DataSourceUtils.getConnection()
     * ???????????? ????????? ???????????? ???????????? ???????????? ????????? ?????? ???????????? ??????
     * ???????????? ????????? ???????????? ???????????? ???????????? ?????? ?????? ????????? ???????????? ???????????? ????????????.
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        // ??????! ???????????? ???????????? ??????????????? DataSourceUtils??? ???????????? ??????.
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass() );
        return con;
    }

    /** DataSourceUtils.releaseConnection()
     * ??????????????? ???????????? ?????? ???????????? ???????????? ???????????? ?????? ?????? ????????? ???????????????.
     * ???????????? ????????? ???????????? ???????????? ???????????? ?????? ?????? ?????? ???????????? ?????????.
     * @param con
     * @param stmt
     * @param rs
     */
    private void close(Connection con, Statement stmt, ResultSet rs)
    {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //??????! ???????????? ???????????? ??????????????? DataSourceUtils??? ???????????? ??????.
        DataSourceUtils.releaseConnection(con, dataSource);

    }

}
