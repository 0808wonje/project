package project.shop.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shop.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
}
