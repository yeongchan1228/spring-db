package springdb.springdb2.item.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.ItemRepository;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.dto.ItemUpdateDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static springdb.springdb2.item.entity.QItem.*;

@Repository
@Transactional
public class JpaRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(em.find(Item.class, id));
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

//        QItem item = new QItem("i");

        return query
                .selectFrom(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName){
        if(StringUtils.hasText(itemName)) return  item.itemName.like("%" + itemName + "%");
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice){
        if(maxPrice != null) return item.price.loe(maxPrice);
        return null;
    }

    public List<Item> findAllOld(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

//        QItem item = new QItem("i");

        BooleanBuilder builder = new BooleanBuilder();
        if(StringUtils.hasText(itemName)) builder.and(item.itemName.like("%" + itemName + "%"));
        if(maxPrice != null) builder.and(item.price.loe(maxPrice));

        return query
                .selectFrom(item)
                .where(builder)
                .fetch();
    }
}
