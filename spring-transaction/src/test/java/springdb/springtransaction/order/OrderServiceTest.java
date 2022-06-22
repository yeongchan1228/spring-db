package springdb.springtransaction.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void complete() throws NotConditionSatisfiedException {
        Order order = new Order();
        order.setUsername("정상");

        orderService.order(order);

        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getStatus()).isEqualTo("완료");
    }
    
    @Test
    void runtimeException() {
        Order order = new Order();
        order.setUsername("예외");
        
        assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);

        Optional<Order> findOrder = orderRepository.findById(order.getId());
        assertThat(findOrder.isEmpty()).isTrue();
    }

    @Test
    void checkedException() {
        Order order = new Order();
        order.setUsername("부족");

        try {
            orderService.order(order);
        } catch (NotConditionSatisfiedException e) {
            log.info("고객에게 정보 전달");
        }

        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getStatus()).isEqualTo("대기");
    }

}