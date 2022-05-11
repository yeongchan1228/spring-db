package springdb.springdb1.service;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springdb.springdb1.domain.Member;
import springdb.springdb1.repository.MemberRepositoryV1;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static springdb.springdb1.connection.ConnectionConst.*;

/**
 * 기본 동작, Transaction 없어서 예외 발생
 */

@Slf4j
class MemberServiceV1Test {

    private static final String MEMBER_A = "memberA";
    private static final String MEMBER_B = "memberB";
    private static final String MEMBER_C = "ex";

    private MemberRepositoryV1 memberRepository;
    private MemberServiceV1 memberService;

    @BeforeEach
    void beforeEach(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        memberRepository = new MemberRepositoryV1(dataSource);
        memberService = new MemberServiceV1(memberRepository);
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
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberC.getMoney()).isEqualTo(10000);
    }
}