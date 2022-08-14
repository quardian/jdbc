package com.inho.jdbc.repository;

import com.inho.jdbc.domain.Member;
import com.inho.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 예외 누수 문제 해결
 * 체크예외를 런타임 예외로 변경
 * MemberRespository 인터페이스 사용
 *
 */
@Slf4j
public class MemberRepositoryV4_1 implements MemberRepository{

    private final DataSource dataSource;

    public MemberRepositoryV4_1(DataSource dataSource) {
        this.dataSource = dataSource;
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
        catch (Exception e)
        {
            log.error("db error", e);
            throw new MyDbException(e);
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
        catch (Exception e)
        {
            log.error("db error", e);
            throw new MyDbException(e);
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
        catch (Exception e)
        {
            log.error("db error", e);
            throw new MyDbException(e);
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
        catch (Exception e)
        {
            log.error("db error", e);
            throw new MyDbException(e);
        }
        finally {
            close(con, pstmt, rs);
        }
    }


    /** DataSourceUtils.getConnection()
     * 트랜잭션 동기화 매니저가 관리하는 커넥션이 있으면 해당 커넥션을 반환
     * 트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우 새로운 커넥션을 생성해서 반환한다.
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        // 주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass() );
        return con;
    }

    /** DataSourceUtils.releaseConnection()
     * 트랜잭션을 사용하기 위해 동기화된 커넥션은 커넥션을 닫지 않고 그대로 유지해준다.
     * 트랜잰션 동기화 매니저가 관리하는 커넥션이 없는 경우 해당 커넥션을 닫는다.
     * @param con
     * @param stmt
     * @param rs
     */
    private void close(Connection con, Statement stmt, ResultSet rs)
    {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        DataSourceUtils.releaseConnection(con, dataSource);

    }

}
