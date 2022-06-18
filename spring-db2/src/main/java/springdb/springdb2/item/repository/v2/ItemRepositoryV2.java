package springdb.springdb2.item.repository.v2;

import org.springframework.data.jpa.repository.JpaRepository;
import springdb.springdb2.item.entity.Item;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
}
