package springdb.springdb1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import springdb.springdb1.domain.Member;
import springdb.springdb1.repository.MemberRepository;

import java.sql.SQLException;

/**
 * 예외 누수 문제 해결
 */
@RequiredArgsConstructor
public class MemberServiceV4 {

    private final MemberRepository memberRepository;

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) {
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
