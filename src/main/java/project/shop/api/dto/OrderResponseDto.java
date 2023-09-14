package project.shop.api.dto;

import lombok.Getter;
import project.shop.domain.Order;
import project.shop.domain.ShippingMethod;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {

  private String memberName;
  private String itemName;
  private int price;
  private int quantity;
  private LocalDateTime orderDate;
  private ShippingMethod shippingMethod;

  public OrderResponseDto(Order order) {
    this.memberName = order.getMember().getNickname();
    this.itemName = order.getItem().getItemName();
    this.price = order.getItem().getPrice();
    this.quantity = order.getItem().getQuantity();
    this.orderDate = order.getCreatedTime();
    this.shippingMethod = order.getShippingMethod();
  }
}
