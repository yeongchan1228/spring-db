package dbstudy.redisstudy.repository;

import dbstudy.redisstudy.entity.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ItemRedisRepository extends CrudRepository<Item, Long> {

    Optional<Item> findByName(String name);
}
