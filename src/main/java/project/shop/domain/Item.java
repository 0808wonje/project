package project.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "member", "itemName", "formattedPrice", "region", "formattedQuantity", "itemStatus"})
public class Item extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long id;

  private String itemName;
  private Integer price;
  private Integer quantity;
  private String description;

  @Enumerated(EnumType.STRING)
  private Region region;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "item", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<UploadFile> images = new ArrayList<>();

  @OneToMany(mappedBy = "item", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<Comment> comments = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id")
  private Order order;

  private String formattedPrice;
  private String formattedQuantity;

  @Enumerated(EnumType.STRING)
  private ItemStatus itemStatus;

  public void assignId(Long id) {
    this.id = id;
  }

  public static Item createItem(String itemName, Integer price, Integer quantity, String description, Region region) {
    return new Item(itemName, price, quantity, description, region);
  }

  public void setImages(UploadFile uploadFile) {
    uploadFile.setItem(this);
    this.images.add(uploadFile);
  }

  public void assignMember(Member member) {
    this.member = member;
    member.getItemList().add(this);
  }

  public void assignOrder(Order order) {
    this.order = order;
  }

  public void dismissOrder() {
    this.order = null;
  }

  public void updateItem(String itemName, Integer price, Integer quantity, String description, Region region) {
    this.itemName = itemName;
    this.price =  price;
    this.quantity = quantity;
    this.description = description;
    this.region = region;
  }

  private Item(String itemName, Integer price, Integer quantity, String description, Region region) {
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;
    this.description = description;
    this.region = region;
    this.itemStatus = ItemStatus.ONSALE;
  }

  public void setFormattedNumber(String price, String formattedQuantity) {
    this.formattedPrice = price;
    this.formattedQuantity = formattedQuantity;
  }

  public void changeItemStatus(ItemStatus itemStatus) {
    this.itemStatus = itemStatus;
  }

}
