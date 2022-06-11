package dbstudy.redisstudy.controller;

import dbstudy.redisstudy.controller.dto.ItemResponseDto;
import dbstudy.redisstudy.controller.dto.SaveItemRequestDto;
import dbstudy.redisstudy.entity.Item;
import dbstudy.redisstudy.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final RedisConnectionFactory redisConnectionFactory;

    @GetMapping
    public String get(){
        Iterable<Item> result = itemService.findAll();
        result.forEach(item -> {
            log.info("item.name = {}", item.getName());
        });
        return result.toString();
    }

    @GetMapping("/one")
    public ItemResponseDto getOne(@RequestParam(value = "id", required = false) Long id,
                                  @RequestParam(value = "name", required = false) String name){
        if(StringUtils.hasText(name)) return ItemResponseDto.of(itemService.findByName(name));

        return ItemResponseDto.of(itemService.findOne(id));
    }

    @PostMapping
    public ItemResponseDto set(@RequestBody SaveItemRequestDto saveItemRequestDto) {
        return ItemResponseDto.of(itemService.save(Item.of(saveItemRequestDto)));
    }
}
