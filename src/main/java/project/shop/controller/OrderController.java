package project.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.shop.domain.*;
import project.shop.service.ItemService;
import project.shop.service.MemberService;
import project.shop.service.OrderService;
import project.shop.web.dto.CreateOrderForm;
import project.shop.web.dto.ItemCommentForm;
import project.shop.web.session.SessionConst;

import javax.servlet.http.HttpServletRequest;

import static project.shop.domain.Order.createOrder;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

  private final MemberService memberService;
  private final ItemService itemService;
  private final OrderService orderService;

  // 주문 요청
  @PostMapping("/item/createOrder/{itemId}")
  public String requestOrder(@PathVariable Long itemId,
                             @ModelAttribute ItemCommentForm itemCommentForm, BindingResult bindingResult1,
                             @Validated @ModelAttribute CreateOrderForm createOrderForm, BindingResult bindingResult2,
                             Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    Long memberId = (Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER_ID);
    Member member = memberService.findOneMember(memberId);
    Item item = itemService.findOneItem(itemId);

    // 자신의 상품을 주문하는 경우 필터링
    if (member.getItemList().stream().filter(i -> i.getId().equals(itemId)).findAny().isPresent()) {
      redirectAttributes.addAttribute("itemId", itemId);
      redirectAttributes.addAttribute("shippingWays", ShippingMethod.values());
      redirectAttributes.addAttribute("selfOrder", true);
      return "redirect:/item/itemInfo/{itemId}";
    }

    // CreateOrderForm 확인
    if (bindingResult2.hasErrors()) {
      log.info("error = {}", bindingResult2);
      model.addAttribute("member", member);
      model.addAttribute("item", item);
      model.addAttribute("shippingWays", ShippingMethod.values());
      return "item/itemInfo";
    }

    // 주문 생성 후 내 주문정보 화면으로 이동
    Order order = createOrder(item, member, createOrderForm.getShippingMethod());
    orderService.saveOrder(order);
    itemService.changeItemStatus(itemId, ItemStatus.REQUEST);

    redirectAttributes.addAttribute("memberId", memberId);
    redirectAttributes.addAttribute("orderRequestSuccess", true);

    return "redirect:/member/myOrders/{memberId}";
  }

  // 주문 취소
  @PostMapping("/member/myItems/{memberId}/deleteOrder")
  public String cancelOrder(@PathVariable Long memberId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    // 아이템에 걸려있는 연관관계를 먼저 해제하고 상태를 ONSALE로 변경.
    Long orderId = Long.parseLong(request.getParameter("cancel-order"));
    Order order = orderService.findOrderById(orderId);
    itemService.changeItemStatus(order.getItem().getId(), ItemStatus.ONSALE);
    itemService.dismissOrder(order.getItem().getId());

    // 이후에 Order 삭제
    orderService.deleteOrder(orderId);

    redirectAttributes.addAttribute("memberId", memberId);
    redirectAttributes.addAttribute("cancel", true);
    return "redirect:/member/myOrders/{memberId}";
  }

  // 거래 수락
  @PostMapping("/acceptRequest/{itemId}")
  public String acceptRequest(@PathVariable Long itemId, RedirectAttributes redirectAttributes) {
    Item item = itemService.findOneItem(itemId);
    itemService.changeItemStatus(itemId, ItemStatus.TRADING);

    redirectAttributes.addAttribute("memberId", item.getMember().getId());
    redirectAttributes.addAttribute("accepted", true);

    return "redirect:/member/myItems/{memberId}";
  }


  // 거래 거절
  @PostMapping("/refuseRequest/{itemId}")
  public String refuseRequest(@PathVariable Long itemId, RedirectAttributes redirectAttributes) {
    // 아이템에 걸려있는 연관관계를 먼저 해제하고 상태를 ONSALE로 변경.
    Item item = itemService.findOneItem(itemId);
    Order order = item.getOrder();
    itemService.changeItemStatus(itemId, ItemStatus.ONSALE);
    itemService.dismissOrder(itemId);

    // 이후에 Order 삭제
    orderService.deleteOrder(order.getId());

    redirectAttributes.addAttribute("memberId", item.getMember().getId());
    redirectAttributes.addAttribute("refused", true);

    return "redirect:/member/myItems/{memberId}";
  }

  // 거래 확정
  @PostMapping("/completeOrder/{itemId}")
  public String completeOrder(@PathVariable Long itemId, RedirectAttributes redirectAttributes) {
    Item item = itemService.findOneItem(itemId);
    itemService.changeItemStatus(itemId, ItemStatus.COMPLETE);

    redirectAttributes.addAttribute("memberId", item.getMember().getId());
    redirectAttributes.addAttribute("orderComplete", true);

    return "redirect:/member/myItems/{memberId}";
  }
}
