package dbstudy.redisstudy.controller.dto;

import dbstudy.redisstudy.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String name;
    public int age;

    List<String> tags;

    public static ItemResponseDto of(Item item){
        return new ItemResponseDto(item.getId(), item.getName(), item.getAge(), item.getTags());
    }
}
