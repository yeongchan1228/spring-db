package springdb.springdb2.item.repository.v1.jpa;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.v1.ItemRepository;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.v1.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

@Transactional
public class JpaRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository springDataJpaItemRepository;

    public JpaRepositoryV2(SpringDataJpaItemRepository springDataJpaItemRepository) {
        this.springDataJpaItemRepository = springDataJpaItemRepository;
    }

    @Override
    public Item save(Item item) {
        return springDataJpaItemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = springDataJpaItemRepository.findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return springDataJpaItemRepository.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        if(StringUtils.hasText(itemName) && maxPrice != null){
            return springDataJpaItemRepository.findItems("%" + itemName + "%", maxPrice);
        }else if(StringUtils.hasText(itemName)){
            return springDataJpaItemRepository.findByItemNameLike("%" + itemName + "%");
        }else if(maxPrice != null){
            return springDataJpaItemRepository.findByPriceLessThanEqual(maxPrice);
        }
        return springDataJpaItemRepository.findAll();
    }
}
