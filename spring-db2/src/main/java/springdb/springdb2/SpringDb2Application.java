package springdb.springdb2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springdb.springdb2.item.config.MemoryConfig;
import springdb.springdb2.item.repository.ItemRepository;

@Import(MemoryConfig.class)
@SpringBootApplication(scanBasePackages = "springdb.springdb2.item.controller")
public class SpringDb2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringDb2Application.class, args);
	}

	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

}
