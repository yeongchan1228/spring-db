package springdb.springtransaction.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    public final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Transactional
    public void joinV1(String username){
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("==== member Repository 실행 시작 ====");
        memberRepository.save(member);
        log.info("==== member Repository 실행 종료 ====");

        log.info("==== log Repository 실행 시작 ====");
        logRepository.save(logMessage);
        log.info("==== log Repository 실행 종료 ====");
    }

    @Transactional
    public void joinV2(String username){
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("==== member Repository 실행 시작 ====");
        memberRepository.save(member);
        log.info("==== member Repository 실행 종료 ====");

        log.info("==== log Repository 실행 시작 ====");
        try {
            logRepository.save(logMessage);
        } catch (RuntimeException e) {
          log.info("log 저장에 실패했습니다. logMessage = {}");
          log.info("정상 흐름 반환");
        }

        log.info("==== log Repository 실행 종료 ====");
    }
}
