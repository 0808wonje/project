package project.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "loginId", "nickname", "password", "address", "phoneNumber"})
public class Member extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  private String loginId;

  private String nickname;
  private String password;

  @Embedded
  private Address address;

  private String phoneNumber;

  @OneToMany(mappedBy = "member", orphanRemoval = true)
  private List<Item> itemList = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();

  public void updateMember(String nickname, String password, Address address, String phoneNumber) {
    this.nickname = nickname;
    this.password = password;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  public static Member createMember(String loginId, String nickname, String password, Address address, String phoneNumber) {
    return new Member(loginId, nickname, password, address, phoneNumber);
  }

  private Member(String loginId, String nickname, String password, Address address, String phoneNumber) {
    this.loginId = loginId;
    this.nickname = nickname;
    this.password = password;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  public void assignId(Long id) {
    this.id = id;
  }

}
