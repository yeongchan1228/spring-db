package springdb.springdb2.item.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springdb.springdb2.item.repository.ItemRepository;
import springdb.springdb2.item.repository.jpa.JpaRepositoryV3;
import springdb.springdb2.item.service.ItemService;
import springdb.springdb2.item.service.ItemServiceV1;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
public class QuerydslConfig {

    private final EntityManager em;

    @Bean
    public ItemService itemService(){
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository(){
        return new JpaRepositoryV3(em);
    }
}
