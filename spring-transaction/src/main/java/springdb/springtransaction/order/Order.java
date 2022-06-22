package springdb.springtransaction.order;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String username;
    private String status;
}
