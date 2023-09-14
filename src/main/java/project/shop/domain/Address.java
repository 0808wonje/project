package project.shop.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"region", "street"})
public class Address {

  private Region district;
  @Column(length = 15)
  private String street;
  @Column(length = 10)
  private String details;

  public Address(Region district, String street) {
    this.district = district;
    this.street = street;
  }
}
