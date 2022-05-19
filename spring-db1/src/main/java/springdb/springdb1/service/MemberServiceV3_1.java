package springdb.springdb1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springdb.springdb1.domain.Member;
import springdb.springdb1.repository.MemberRepositoryV3;

import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@RequiredArgsConstructor
public class MemberServiceV3_1 {
    private final MemberRepositoryV3 memberRepository;
    private final PlatformTransactionManager transactionManager;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 비즈니스 로직
            bizLogic(fromId, toId, money);
            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
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

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        // 같은 커넥션에서 동작해야하고 하나의 트랜잭션에서 동작해야 한다.
        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member member) {
        if(member.getMemberId().equals("ex")){
            throw new IllegalStateException("이체 중 예외");
        }
    }
}
