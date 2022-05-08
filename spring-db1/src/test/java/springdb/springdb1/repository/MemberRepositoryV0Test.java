package springdb.springdb1.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import springdb.springdb1.domain.Member;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @Test
    public void crud() throws Exception {
        // save
        Member member = new Member("memberV0", 10000);
        memberRepositoryV0.save(member);

        // findById
        Member findMember = memberRepositoryV0.findById(member.getMemberId());
        assertThat(member.getMoney()).isEqualTo(findMember.getMoney());

        // update
        memberRepositoryV0.update(member.getMemberId(), 20000);
        Member findUpdateMember = memberRepositoryV0.findById(member.getMemberId());
        assertThat(findUpdateMember.getMoney()).isEqualTo(20000);

        // delete
        memberRepositoryV0.delete(member.getMemberId());
        assertThatThrownBy(() -> memberRepositoryV0.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }

}