package springdb.springdb2.item.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springdb.springdb2.item.repository.ItemRepository;
import springdb.springdb2.item.repository.jdbctemplate.JdbcTemplateRepositoryV1;
import springdb.springdb2.item.service.ItemService;
import springdb.springdb2.item.service.ItemServiceV1;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateV1Config {

    private final DataSource dataSource;

    @Bean
    public ItemService itemService(){
        return new ItemServiceV1(itemRepository( ));
    }

    @Bean
    public ItemRepository itemRepository(){
        return new JdbcTemplateRepositoryV1(dataSource);
    }
}
