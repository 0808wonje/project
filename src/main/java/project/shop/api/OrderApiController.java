package project.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.shop.api.dto.OrderResponseDto;
import project.shop.api.dto.Result;
import project.shop.domain.Item;
import project.shop.domain.ItemStatus;
import project.shop.domain.Member;
import project.shop.domain.Order;
import project.shop.domain.exceptions.ItemStatusException;
import project.shop.service.ItemService;
import project.shop.service.MemberService;
import project.shop.service.OrderService;
import project.shop.web.dto.CreateOrderForm;

import java.util.List;

import static java.util.stream.Collectors.*;
import static project.shop.domain.Order.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final MemberService memberService;
  private final ItemService itemService;
  private final OrderService orderService;

  // 주문 요청
  @PostMapping("/api/orderRequest/{memberId}/{itemId}")
  public OrderResponseDto requestOrder(@PathVariable Long memberId, @PathVariable Long itemId,
                                       @RequestBody @Validated CreateOrderForm createOrderForm) {
    // 멤버, 상품 조회
    Member member = memberService.findOneMember(memberId);
    Item item = itemService.findOneItem(itemId);
    if (item.getMember().equals(member)) {
      throw new ItemStatusException("자신의 상품은 주문할 수 없습니다");
    }

    // 주문 생성 및 초기화 이후 저장
    Order order = createOrder(item, member, createOrderForm.getShippingMethod());
    order.assignMemberAndItem(member, item);
    orderService.saveOrder(order);
    itemService.changeItemStatus(itemId, ItemStatus.REQUEST);

    return new OrderResponseDto(order);
  }

  // 주문 취소
  @PostMapping("/api/orderCancel/{orderId}")
  public String cancelOrder(@PathVariable Long orderId) {
    // 주문 조회
    Order order = orderService.findOrderById(orderId);
    if (!(order.getItem().getItemStatus().equals(ItemStatus.REQUEST))) {
      throw new ItemStatusException("REQUEST 상태에서만 취소 가능합니다");
    }
    // 주문에서 상품 연관관계 해제 후 삭제
    itemService.changeItemStatus(order.getItem().getId(), ItemStatus.ONSALE);
    itemService.dismissOrder(order.getItem().getId());

    return "Successfully Deleted";
  }

  // 거래 수락
  @PostMapping("/api/requestAccept/{itemId}")
  public String acceptRequest(@PathVariable Long itemId) {
    Item item = itemService.findOneItem(itemId);
    if (!(item.getItemStatus().equals(ItemStatus.REQUEST))) {
      throw new ItemStatusException("REQUEST 상태에서만 수락 가능합니다");
    }
    itemService.changeItemStatus(itemId, ItemStatus.TRADING);
    return "Successfully Accepted";
  }


  // 거래 거절
  @PostMapping("/api/requestRefuse/{orderId}")
  public String refuseRequest(@PathVariable Long orderId) {
    Order order = orderService.findOrderById(orderId);
    if (!(order.getItem().getItemStatus().equals(ItemStatus.REQUEST))) {
      throw new ItemStatusException("REQUEST 상태에서만 거절 가능합니다");
    }
    itemService.changeItemStatus(order.getItem().getId(), ItemStatus.ONSALE);
    itemService.dismissOrder(order.getItem().getId());

    return "Successfully Refused";
  }

  // 거래 확정
  @PostMapping("/api/orderComplete/{itemId}")
  public String completeOrder(@PathVariable Long itemId) {
    Item item = itemService.findOneItem(itemId);
    if (!(item.getItemStatus().equals(ItemStatus.TRADING))) {
      throw new ItemStatusException("TRADING 상태에서만 확정 가능합니다");
    }
    itemService.changeItemStatus(itemId, ItemStatus.COMPLETE);
    return "Successfully Completed";
  }


  // 특정 회원의 주문 조회
  @GetMapping("/api/allOrder/{memberId}")
  public Result getMembersAllOrder(@PathVariable Long memberId) {
    List<Order> orders = orderService.findMembersAllOrder(memberId);
    List<OrderResponseDto> dtos = orders.stream().map(OrderResponseDto::new).collect(toList());
    return new Result(dtos.size(), dtos);
  }

  // 모든 주문 조회
  @GetMapping("/api/allOrder")
  public Result getAllOrder() {
    List<Order> orders = orderService.findAllOrderForApi();
    List<OrderResponseDto> dtos = orders.stream().map(OrderResponseDto::new).collect(toList());
    return new Result(dtos.size(), dtos);
  }
}
