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
import springdb.springdb1.repository.MemberRepositoryV3;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DataSource 자동 등록
 */

@Slf4j
@SpringBootTest
class MemberServiceV3_4Test {

    private static final String MEMBER_A = "memberA";
    private static final String MEMBER_B = "memberB";
    private static final String MEMBER_C = "ex";

    @Autowired private MemberRepositoryV3 memberRepository;
    @Autowired private MemberServiceV3_3 memberService;

    @TestConfiguration
    static class testConfiguration {
        @Autowired private DataSource dataSource;

        @Bean
        MemberServiceV3_3 memberService() {
            return new MemberServiceV3_3(memberRepository());
        }

        @Bean
        MemberRepositoryV3 memberRepository() {
            return new MemberRepositoryV3(dataSource);
        }
    }

    @AfterEach
    void afterEach() throws SQLException {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("정상 이체")
    public void accountTransfer() throws Exception {
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
    public void accountTransferException() throws Exception {
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