package springdb.springtransaction.propagation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired LogRepository logRepository;

    /**
     * memberService : @Trasactional : X
     * memberRepository : @Transactional : O
     * logRepository : @Transactional : O
     */
    @Test
    void case1(){
        String username = "case1";
        memberService.joinV1(username);

        assertThat(memberRepository.findByUsername(username)).isNotEmpty();
        assertThat(logRepository.findByMessage(username)).isNotEmpty();
    }

    /**
     * memberService : @Trasactional : X
     * memberRepository : @Transactional : O
     * logRepository : @Transactional : O 예외 발생
     */
    @Test
    void case2(){
        String username = "예외";

        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        assertThat(memberRepository.findByUsername(username)).isNotEmpty();
        assertThat(logRepository.findByMessage(username)).isEmpty();
    }

    /**
     * memberService : @Trasactional : O
     * memberRepository : @Transactional : X
     * logRepository : @Transactional : X
     */
    @Test
    void case3(){
        String username = "case3";
        memberService.joinV1(username);

        assertThat(memberRepository.findByUsername(username)).isNotEmpty();
        assertThat(logRepository.findByMessage(username)).isNotEmpty();
    }

    /**
     * memberService : @Trasactional : O
     * memberRepository : @Transactional : O
     * logRepository : @Transactional : O
     */
    @Test
    void case4(){
        String username = "case4";
        memberService.joinV1(username);

        assertThat(memberRepository.findByUsername(username)).isNotEmpty();
        assertThat(logRepository.findByMessage(username)).isNotEmpty();
    }

    /**
     * memberService : @Trasactional : O
     * memberRepository : @Transactional : O
     * logRepository : @Transactional : O 예외
     */
    @Test
    void case5(){
        String username = "예외2";

        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        // 모든 데이터 롤백
        assertThat(memberRepository.findByUsername(username)).isEmpty();
        assertThat(logRepository.findByMessage(username)).isEmpty();
    }

    /**
     * memberService : @Trasactional : O
     * memberRepository : @Transactional : O
     * logRepository : @Transactional : O 예외
     */
    @Test
    void case6(){
        String username = "예외3";

        // LogRepository에서 rollback-only를 마크 했기 때문에 예외를 잡아 처리해도 롤백이된다.
        // joinV2에서 예외를 처리했기 때문에 물리 트랜잭션은 커밋을 진행하지만 rollback-only가 마크되어 있기 때문에 해당 예외 발생
        assertThatThrownBy(() -> memberService.joinV2(username)).isInstanceOf(UnexpectedRollbackException.class);

        // 모든 데이터 롤백
        assertThat(memberRepository.findByUsername(username)).isEmpty();
        assertThat(logRepository.findByMessage(username)).isEmpty();
    }

    /**
     * memberService : @Trasactional : O
     * memberRepository : @Transactional : O
     * logRepository : @Transactional : O 예외
     */
    @Test
    void case7(){
        String username = "예외4";

        // propagation = REQUIRED_NEW 사용
        memberService.joinV2(username);

        // 모든 데이터 롤백
        assertThat(memberRepository.findByUsername(username)).isNotEmpty();
        assertThat(logRepository.findByMessage(username)).isEmpty();
    }

}