package project.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.domain.Order;
import project.shop.repository.order.JpaOrderRepository;
import project.shop.repository.order.OrderRepository;
import project.shop.repository.order.apiquery.OrderQueryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

  @Transactional
  public void saveOrder(Order order) {
    orderRepository.save(order);
  }

  public Order findOrderById(Long orderId) {
    return orderRepository.findById(orderId).orElse(null);
  }

  @Transactional
  public void deleteOrder(Long orderId) {
    orderRepository.deleteById(orderId);
  }

  // API용 주문조회 메서드 (주문만 출력)
  public List<Order> findAllOrderForApi() {
    return orderQueryRepository.findAllOrderForApi();
  }

  // API용 주문조회 메서드 (주문+멤버 출력)
  public List<Order> findMembersAllOrder(Long memberId) {
    return orderQueryRepository.findMembersAllOrder(memberId);
  }
}
