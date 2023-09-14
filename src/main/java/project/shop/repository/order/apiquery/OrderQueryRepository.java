package project.shop.repository.order.apiquery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.shop.domain.Order;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

  private final EntityManager em;

  public List<Order> findAllOrderForApi() {
    String jpql = "select o from Order o join fetch o.member m join fetch o.item i";
    return em.createQuery(jpql, Order.class).getResultList();
  }

  public List<Order> findMembersAllOrder(Long memberId) {
    String jpql = "select o from Order o join fetch o.member m join fetch o.item i where m.id = :memberId";
    return em.createQuery(jpql, Order.class).setParameter("memberId", memberId).getResultList();
  }
}
