package project.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shop.domain.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
