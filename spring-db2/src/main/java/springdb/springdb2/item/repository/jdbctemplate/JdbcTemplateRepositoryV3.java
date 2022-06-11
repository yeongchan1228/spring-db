package springdb.springdb2.item.repository.jdbctemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.ItemRepository;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.dto.ItemUpdateDto;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SimpleJdbcInsert 사용
 */
@Slf4j
public class JdbcTemplateRepositoryV3 implements ItemRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcTemplateRepositoryV3(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("item")
                .usingGeneratedKeyColumns("id"); // Pk
//                .usingColumns("item_name", "price", "quantity") // 생략 가능
    }

    @Override
    public Item save(Item item) {
        Number key = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(item));
        item.setId(key.longValue());
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";

        namedParameterJdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("itemName", updateParam.getItemName())
                        .addValue("price", updateParam.getPrice())
                        .addValue("quantity", updateParam.getQuantity())
                        .addValue("id", itemId));
    }

    @Override
    public Optional<Item> findById(Long id) {

        String sql = "select id, item_name as itemName, price, quantity from item where id=:id";

        try{
            Item item = namedParameterJdbcTemplate.queryForObject(sql,
                    Map.of("id", id), itemRowMapper());
            return Optional.of(item);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(cond);

        String sql = "select * from item";

        // 동적 쿼리
        if(StringUtils.hasText(itemName) || maxPrice != null){
            sql += " where";
        }

        boolean andFlag = false;
        if(StringUtils.hasText(itemName)){
            sql += " item_name like concat('%', :itemName, '%')";
            andFlag = true;
        }

        if(maxPrice != null){
            if(andFlag){
                sql += " and price <= :maxPrice";
            }else{
                sql += " price <= :maxPrice";
            }
        }

        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, itemRowMapper());
    }

    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class); // camel 표기법으로 맞춰준다. 언더스코어(item_id) -> camel(itemId)
    }
}
