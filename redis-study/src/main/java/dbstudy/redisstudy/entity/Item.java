package dbstudy.redisstudy.entity;

import dbstudy.redisstudy.controller.dto.SaveItemRequestDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@Getter
@RedisHash(value = "item", timeToLive = 60) // 다른 객체와의 id 충돌을 막아줌, 초 단위
public class Item {
    @Id
    private Long id; // RedisHash에 의하여 item:id값으로 저장됨

    @Indexed // 해당 컬럼은 findBy~로 조회 가능
    private String name;
    public int age;

    List<String> tags;

    @Builder(builderMethodName = "createItem")
    public Item(Long id, String name, int age, List<String> tags) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.tags = tags;
    }

    public static Item of(SaveItemRequestDto itemRequestDto){
        return Item.createItem()
                .id(itemRequestDto.getId())
                .name(itemRequestDto.getName())
                .age(itemRequestDto.getAge())
                .tags(itemRequestDto.getTags())
                .build();
    }
}
