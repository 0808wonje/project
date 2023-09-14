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

  public List<Order> findAllOrderForApi() {
    return orderQueryRepository.findAllOrderForApi();
  }

  public List<Order> findMembersAllOrder(Long memberId) {
    return orderQueryRepository.findMembersAllOrder(memberId);
  }
}
