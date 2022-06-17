package springdb.springdb2.item.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springdb.springdb2.item.repository.ItemRepository;
import springdb.springdb2.item.repository.jpa.JpaRepositoryV2;
import springdb.springdb2.item.repository.jpa.SpringDataJpaItemRepository;
import springdb.springdb2.item.service.ItemService;
import springdb.springdb2.item.service.ItemServiceV1;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {

    private final SpringDataJpaItemRepository springDataJpaItemRepository;

    @Bean
    public ItemService itemService(){
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository(){
        return new JpaRepositoryV2(springDataJpaItemRepository);
    }
}
