package project.shop.api.dto;

import lombok.Getter;
import project.shop.domain.Address;
import project.shop.domain.Member;

import java.util.List;

import static java.util.stream.Collectors.*;

@Getter
public class MemberResponseDto {

  private String loginId;
  private String nickname;
  private String password;
  private Address address;
  private String phoneNumber;
  private List<ItemResponseDto> itemDtoList;
  private List<OrderResponseDto> orderDtoList;

  public MemberResponseDto(Member member) {
    this.loginId = member.getLoginId();
    this.nickname = member.getNickname();
    this.password = member.getPassword();
    this.address = member.getAddress();
    this.phoneNumber = member.getPhoneNumber();
    this.itemDtoList = member.getItemList().stream().map(ItemResponseDto::new).collect(toList());
    this.orderDtoList = member.getOrders().stream().map(OrderResponseDto::new).collect(toList());
  }
}
