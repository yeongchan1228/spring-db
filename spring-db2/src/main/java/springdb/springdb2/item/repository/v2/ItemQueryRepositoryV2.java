package springdb.springdb2.item.repository.v2;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.ItemSearchCond;

import javax.persistence.EntityManager;
import java.util.List;

import static springdb.springdb2.item.entity.QItem.*;

@Repository
public class ItemQueryRepositoryV2 {
    private final JPQLQueryFactory query;

    public ItemQueryRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond){
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        BooleanBuilder builder = new BooleanBuilder();
        if(StringUtils.hasText(itemName)) builder.and(item.itemName.like("%" + itemName + "%"));
        if(maxPrice != null) builder.and(item.price.loe(maxPrice));

        return query
                .selectFrom(item)
                .where(builder)
                .fetch();
    }
}
