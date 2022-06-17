package springdb.springdb2.item.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity // orm 추가
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // orm 추가
    private Long id;

    @Column(length = 10) // orm 추가
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
