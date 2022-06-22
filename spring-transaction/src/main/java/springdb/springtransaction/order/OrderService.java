package springdb.springtransaction.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public void order(Order order) throws NotConditionSatisfiedException {
        log.info("===order 실행===");
        orderRepository.save(order);

        log.info("===결제===");
        if (order.getUsername().equals("예외")) {
            log.info("===시스템 예외 발생===");
            throw new RuntimeException();
        } else if (order.getUsername().equals("부족")) {
            log.info("===부족 처리===");
            order.setStatus("대기");
            throw new NotConditionSatisfiedException("조건을 만족하지 못했습니다.");
        } else {
            log.info("===정상 처리===");
            order.setStatus("완료");
        }
        log.info("===종료===");
    }
}
