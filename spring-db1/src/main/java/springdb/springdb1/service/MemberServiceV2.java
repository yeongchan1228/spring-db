package springdb.springdb1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import springdb.springdb1.domain.Member;
import springdb.springdb1.repository.MemberRepositoryV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 파라미터 연동, 풀을 고려한 종료
 */
@RequiredArgsConstructor
@Transactional
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false); // 트랜잭션 시작

            // 비즈니스 로직
            bizLogic(con, fromId, toId, money);
            con.commit(); // 성공 시

        }catch (Exception e){
            con.rollback(); // 실패 시 롤백
            throw e;
        }finally {
            release(con);
        }
        
//        Member fromMember = memberRepository.findById(fromId);
//        Member toMember = memberRepository.findById(toId);
//
//        // 같은 커넥션에서 동작해야하고 하나의 트랜잭션에서 동작해야 한다.
//        memberRepository.update(fromId, fromMember.getMoney() - money);
//        validation(toMember);
//        memberRepository.update(toId, toMember.getMoney() + money);

        // 커밋, 롤백 판단 후 처리
    }

    private void bizLogic(Connection con,String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        // 같은 커넥션에서 동작해야하고 하나의 트랜잭션에서 동작해야 한다.
        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void validation(Member member) {
        if(member.getMemberId().equals("ex")){
            throw new IllegalStateException("이체 중 예외");
        }
    }

    private void release(Connection con) {
        if(con != null){
            try {
                con.setAutoCommit(true); // false로 남기 때문에 true 변경 후 반환
                con.close();
            }catch (Exception e){

            }
        }
    }
}
