package dbstudy.redisstudy.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveItemRequestDto {

    private Long id;
    private String name;
    public int age;

    List<String> tags;
}
