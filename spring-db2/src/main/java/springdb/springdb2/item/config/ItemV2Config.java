package springdb.springdb2.item.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springdb.springdb2.item.repository.v1.ItemRepository;
import springdb.springdb2.item.repository.v1.jpa.JpaRepositoryV3;
import springdb.springdb2.item.repository.v2.ItemQueryRepositoryV2;
import springdb.springdb2.item.repository.v2.ItemRepositoryV2;
import springdb.springdb2.item.service.ItemServiceV2;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
public class ItemV2Config {

    private final EntityManager em;
    private final ItemRepositoryV2 itemRepositoryV2;

    @Bean
    public ItemServiceV2 itemService(){
        return new ItemServiceV2(itemRepositoryV2, itemQueryRepositoryV2());
    }

    @Bean
    public ItemQueryRepositoryV2 itemQueryRepositoryV2(){
        return new ItemQueryRepositoryV2(em);
    }

    // TestInit을 위한 추가  
    @Bean
    public ItemRepository itemRepository(){
        return new JpaRepositoryV3(em);
    }
}
