package springdb.springdb2.item.repository;

import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond cond);

}
