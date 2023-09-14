package project.shop.api.dto;

import lombok.Getter;
import project.shop.domain.Item;
import project.shop.domain.ItemStatus;

import java.time.LocalDateTime;

@Getter
public class ItemResponseDto {

  private Long itemId;
  private String itemName;
  private Integer price;
  private Integer quantity;
  private LocalDateTime createdTime;
  private ItemStatus itemStatus;

  public ItemResponseDto(Item item) {
    this.itemId = item.getId();
    this.itemName = item.getItemName();
    this.price = item.getPrice();
    this.quantity = item.getQuantity();
    this.createdTime = item.getCreatedTime();
    this.itemStatus = item.getItemStatus();
  }
}
