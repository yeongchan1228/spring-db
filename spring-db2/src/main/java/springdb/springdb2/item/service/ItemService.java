package springdb.springdb2.item.service;

import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.v1.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);
}
