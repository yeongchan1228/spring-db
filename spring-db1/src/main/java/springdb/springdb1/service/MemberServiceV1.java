package springdb.springdb1.service;

import lombok.RequiredArgsConstructor;
import springdb.springdb1.domain.Member;
import springdb.springdb1.repository.MemberRepositoryV1;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        // 같은 커넥션에서 동작해야하고 하나의 트랜잭션에서 동작해야 한다.
        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);

        // 커밋, 롤백 판단 후 처리
    }

    private void validation(Member member) {
        if(member.getMemberId().equals("ex")){
            throw new IllegalStateException("이체 중 예외");
        }
    }
}
