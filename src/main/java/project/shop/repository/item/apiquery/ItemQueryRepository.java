package project.shop.repository.item.apiquery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.shop.domain.Item;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

  private final EntityManager em;

  public List<Item> findAllItemForApi() {
    String jpql = "select i from Item i join fetch i.member m";
    return em.createQuery(jpql, Item.class).getResultList();
  }
}
