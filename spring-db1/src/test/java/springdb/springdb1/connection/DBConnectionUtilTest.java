package springdb.springdb1.connection;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DBConnectionUtilTest {

    @Test
    public void connection() throws Exception {
        // given
        Connection connection = DBConnectionUtil.getConnection();
        // when

        // then
        assertThat(connection).isNotNull();
    }
}