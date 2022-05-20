package springdb.springdb1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springdb.springdb1.domain.Member;
import springdb.springdb1.repository.MemberRepository;
import springdb.springdb1.repository.MemberRepositoryV4_1;
import springdb.springdb1.repository.MemberRepositoryV4_2;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Exception Translator 사용
 */

@Slf4j
@SpringBootTest
class MemberServiceV4_1Test {

    private static final String MEMBER_A = "memberA";
    private static final String MEMBER_B = "memberB";
    private static final String MEMBER_C = "ex";

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberServiceV4 memberService;

    @TestConfiguration
    static class testConfiguration {
        @Autowired private DataSource dataSource;

        @Bean
        MemberServiceV4 memberService() {
            return new MemberServiceV4(memberRepository());
        }

        @Bean
        MemberRepositoryV4_2 memberRepository() {
            return new MemberRepositoryV4_2(dataSource);
        }
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("정상 이체")
    public void accountTransfer() {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        // then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB= memberRepository.findById(memberB.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외")
    public void accountTransferException() {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberC = new Member(MEMBER_C, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberC);

        // then
        assertThatThrownBy(() ->
                memberService.accountTransfer(memberA.getMemberId(), memberC.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberC= memberRepository.findById(memberC.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberC.getMoney()).isEqualTo(10000);
    }
}