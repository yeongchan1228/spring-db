package springdb.springdb1.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import springdb.springdb1.domain.Member;
import springdb.springdb1.repository.exception.MyException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 예외 누수 문제 해결
 * check 예외 -> uncheck 예외
외*/
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV4_1 implements MemberRepository{

    private final DataSource dataSource;

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null; // 파라미터 바인딩 가능한 stmt

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            int count = pstmt.executeUpdate(); // 영향을 받은 row 수 반환
            return member;
        }catch (SQLException e){
            throw new MyException(e);
        }finally {
            close(con, pstmt, null);
        }

    }
    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }
        } catch (SQLException e) {
            throw new MyException(e);
        }finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void update(String memberId, int money){
        String sql = "update member set money = ? where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        }catch (SQLException e){
            throw new MyException(e);
        }finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        }catch (SQLException e){
            throw new MyException(e);
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void deleteAll() {
        String sql = "delete from member";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = pstmt.executeUpdate();
        }catch (SQLException e){
            throw new MyException(e);
        }finally {
            close(con, pstmt, null);
        }

    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        // Connection을 꺼버림, 유지되지 않는다.
//        JdbcUtils.closeConnection(con);
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private Connection getConnection() throws SQLException {
        // 주의! 트랜잭션 동기 -> 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용
        // TransactionSynchronizationManager에 Connection이 존재하면 해당 Connection 반환, 없으면 만들어서 반환.
        Connection connection = DataSourceUtils.getConnection(dataSource); // 동기화 적용
        log.info("connection = {}, class = {}", connection, connection.getClass());
        return connection;
    }
}
