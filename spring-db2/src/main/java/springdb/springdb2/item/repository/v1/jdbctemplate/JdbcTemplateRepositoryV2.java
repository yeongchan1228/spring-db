package springdb.springdb2.item.repository.v1.jdbctemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.v1.ItemRepository;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.v1.dto.ItemUpdateDto;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NamedParameterJdbcTemplate
 */
@Slf4j
public class JdbcTemplateRepositoryV2 implements ItemRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcTemplateRepositoryV2(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values (:itemName, :price, :quantity)";

        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(item);

        KeyHolder keyHolder = new GeneratedKeyHolder(); // Database에서 생성한 자동 키 값을 받기 위해 생성
        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);

        item.setId(keyHolder.getKey().longValue());

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
