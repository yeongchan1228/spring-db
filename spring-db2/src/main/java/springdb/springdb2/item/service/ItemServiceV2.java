package springdb.springdb2.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.v1.dto.ItemUpdateDto;
import springdb.springdb2.item.repository.v2.ItemQueryRepositoryV2;
import springdb.springdb2.item.repository.v2.ItemRepositoryV2;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceV2 implements ItemService{

    private final ItemRepositoryV2 itemRepository;
    private final ItemQueryRepositoryV2 queryRepository;

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = itemRepository.findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCond itemSearch) {
        return queryRepository.findAll(itemSearch);
    }
}
