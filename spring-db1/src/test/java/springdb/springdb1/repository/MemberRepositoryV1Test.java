package springdb.springdb1.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springdb.springdb1.domain.Member;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springdb.springdb1.connection.ConnectionConst.*;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepositoryV1;

    @BeforeEach
    void beforeEach(){
        // 매번 connection을 만들어 주기 때문에 성능 상 문제 발생 가능성
//        memberRepositoryV1 = new MemberRepositoryV1(new DriverManagerDataSource(URL, USERNAME, PASSWORD));

        // Connection Pool 사용
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    public void crud() throws Exception {
        // save
        Member member = new Member("memberV0", 10000);
        memberRepositoryV1.save(member);

        // findById
        Member findMember = memberRepositoryV1.findById(member.getMemberId());
        assertThat(member.getMoney()).isEqualTo(findMember.getMoney());

        // update
        memberRepositoryV1.update(member.getMemberId(), 20000);
        Member findUpdateMember = memberRepositoryV1.findById(member.getMemberId());
        assertThat(findUpdateMember.getMoney()).isEqualTo(20000);

        // delete
        memberRepositoryV1.delete(member.getMemberId());
        assertThatThrownBy(() -> memberRepositoryV1.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }

}