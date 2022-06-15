package springdb.springdb2.item.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import springdb.springdb2.item.entity.Item;
import springdb.springdb2.item.repository.ItemSearchCond;
import springdb.springdb2.item.repository.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {
    // xml 불러서 실행
    // 파라미터가 한 개일 경우 @Param 생략 가능
    void save(Item item);

    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateDto);

    @Select("select id, item_name, price, quantity from item where id=#{id}")
    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond itemSearchCond);
}
