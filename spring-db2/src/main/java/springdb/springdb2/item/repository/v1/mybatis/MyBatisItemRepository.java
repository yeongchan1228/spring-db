package springdb.springdb2.item.repository.v1.mybatis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.v1.ItemRepository;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.v1.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository {

    private final ItemMapper itemMapper;

    @Override
    public Item save(Item item) {
        itemMapper.save(item);
        return item;
    }

    @Override
    public void update(Long id, ItemUpdateDto updateDto) {
        itemMapper.update(id, updateDto);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond itemSearchCond) {
        return itemMapper.findAll(itemSearchCond);
    }
}
