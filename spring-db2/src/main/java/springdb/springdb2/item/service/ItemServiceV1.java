package springdb.springdb2.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.v1.ItemRepository;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.v1.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceV1 implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemRepository.update(itemId, updateParam);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        return itemRepository.findAll(cond);
    }
}
