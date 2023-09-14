package project.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "member", "item", "shippingMethod"})
public class Order extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
  private Item item; // 일대일

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member; // 일대다

  @Enumerated(EnumType.STRING)
  private ShippingMethod shippingMethod;

  public void assignMemberAndItem(Member member, Item item) {
    member.getOrders().add(this);
    item.assignOrder(this);
  }

  public static Order createOrder(Item item, Member member, ShippingMethod shippingMethod) {
    return new Order(item, member, shippingMethod);
  }

  private Order(Item item, Member member, ShippingMethod shippingMethod) {
    this.item = item;
    this.member = member;
    this.shippingMethod = shippingMethod;
    assignMemberAndItem(member,item);
  }

}
