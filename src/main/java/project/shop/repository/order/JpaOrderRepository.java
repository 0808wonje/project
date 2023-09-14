package project.shop.repository.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.shop.domain.Order;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaOrderRepository {

  private final EntityManager em;

  public void saveOrder(Order order) {
    em.persist(order);
    log.info("주문 저장 성공");
  }

  public Optional<Order> findOneOrderById(Long orderId) {
    return Optional.ofNullable(em.find(Order.class, orderId));
  }

  public void removeOrder(Long orderId) {
    Optional<Order> order = findOneOrderById(orderId);

    if (order.isPresent()) {
      em.remove(order.get());
    }
  }
}
