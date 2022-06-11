package dbstudy.redisstudy.service;

import dbstudy.redisstudy.entity.Item;
import dbstudy.redisstudy.repository.ItemRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ItemService {

    public final ItemRedisRepository itemRedisRepository;

    public Iterable<Item> findAll(){
        return itemRedisRepository.findAll();
    }

    public Item findOne(Long id){
        return itemRedisRepository.findById(id).orElseThrow(() ->
            {throw new NoSuchElementException("존재하지 않는 값입니다.");}
        );
    }

    public Item findByName(String name){
        return itemRedisRepository.findByName(name).orElseThrow(() ->
                {throw new NoSuchElementException("존재하지 않는 값입니다.");}
        );
    }

    public Item save(Item item){
        return itemRedisRepository.save(item);
    }

}
