package project.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.shop.domain.*;
import project.shop.service.*;
import project.shop.web.dto.ItemCommentForm;
import project.shop.web.dto.ItemUpdateForm;
import project.shop.web.dto.MemberUpdateForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

  private final MemberService memberService;
  private final ItemService itemService;

  // 마이페이지 화면으로 이동
  @GetMapping("/{memberId}")
  public String goMypage(@PathVariable Long memberId, Model model) {
    Member member = memberService.findOneMember(memberId);
    model.addAttribute("member", member);
    return "member/memberPage";
  }

  // 개인정보수정 화면으로 이동
  @GetMapping("/edit/{memberId}")
  public String goUpdatePage(@PathVariable Long memberId, @ModelAttribute MemberUpdateForm memberUpdateForm, Model model) {
    Member member = memberService.findOneMember(memberId);
    memberUpdateForm.setNickname(member.getNickname());
    memberUpdateForm.setPassword(member.getPassword());
    memberUpdateForm.setDistrict(member.getAddress().getDistrict());
    memberUpdateForm.setStreet(member.getAddress().getStreet());
    memberUpdateForm.setDetails(member.getAddress().getDetails());
    memberUpdateForm.setPhoneNumber(member.getPhoneNumber());
    model.addAttribute("member", member);
    model.addAttribute("regions", Region.values());
    return "member/memberEditPage";
  }

  // 개인정보수정
  @PostMapping("/edit/{memberId}")
  public String updateMember(@PathVariable Long memberId, @Validated @ModelAttribute MemberUpdateForm memberUpdateForm,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("member", memberService.findOneMember(memberId));
      model.addAttribute("regions", Region.values());
      log.info("error = {}", bindingResult);
      return "member/memberEditPage";
    }

    memberService.updateMember(memberId, memberUpdateForm);

    redirectAttributes.addAttribute("updated", true);
    redirectAttributes.addAttribute("memberId", memberId);
    return "redirect:/member/{memberId}";
  }

  // 내 아이템 화면으로 이동
  @GetMapping("/myItems/{memberId}")
  public String myItemPage(@PathVariable Long memberId, Model model) {
    Member member = memberService.findOneMember(memberId);
    model.addAttribute("member", member);
    model.addAttribute("items", member.getItemList());
    model.addAttribute("itemStatus", ItemStatus.values());
    return "member/memberItemPage";
  }

  // 아이템 삭제
  @PostMapping("/myItems/{memberId}/deleteItem")
  public String deleteItem(@PathVariable Long memberId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    String[] itemIdArray = request.getParameterValues("delete-item");

    // 아무것도 선택되지 않은 경우
    if (itemIdArray == null) {
      redirectAttributes.addAttribute("nothingSelected", true);
      return "redirect:/member/myItems/{memberId}";
    }

    for (String itemId : itemIdArray) {
      // 거래중인 아이템을 삭제하려고 한 경우
      if (itemService.findOneItem(Long.parseLong(itemId)).getItemStatus().equals(ItemStatus.TRADING)) {
        redirectAttributes.addAttribute("deleteTradingItemError", true);
        continue;
      }
      itemService.deleteItem(Long.parseLong(itemId));
    }

    redirectAttributes.addAttribute("memberId", memberId);
    redirectAttributes.addAttribute("deleted", true);

    return "redirect:/member/myItems/{memberId}";
  }

  // 개별 아이템 수정화면으로 이동
  @GetMapping("/myItems/edit/{itemId}")
  public String itemUpdatePage(@PathVariable Long itemId,
                               @ModelAttribute ItemUpdateForm itemUpdateForm, @ModelAttribute ItemCommentForm itemCommentForm,
                               Model model) {
    Item item = itemService.findOneItem(itemId);
    itemUpdateForm.setItemName(item.getItemName());
    itemUpdateForm.setPrice(item.getPrice());
    itemUpdateForm.setQuantity(item.getQuantity());
    itemUpdateForm.setRegion(item.getRegion());
    itemUpdateForm.setDescription(item.getDescription());
    model.addAttribute("item", item);
    model.addAttribute("regions", Region.values());
    return "member/memberItemEditPage";
  }

  // 개별 아이템 수정
  @PostMapping("/myItems/edit/{itemId}")
  public String itemUpdate(@PathVariable Long itemId,
                           @Validated @ModelAttribute ItemUpdateForm itemUpdateForm, BindingResult bindingResult1,
                           @ModelAttribute ItemCommentForm itemCommentForm, BindingResult bindingResult2,
                           Model model, RedirectAttributes redirectAttributes) throws IOException {
    Item item = itemService.findOneItem(itemId);

    // ItemUpdateForm 확인
    if (bindingResult1.hasErrors()) {
      model.addAttribute("member", item.getMember());
      model.addAttribute("item", item);
      model.addAttribute("regions", Region.values());
      return "member/memberItemEditPage";
    }

    itemService.updateItem(itemId, itemUpdateForm);

    redirectAttributes.addAttribute("updated", true);
    redirectAttributes.addAttribute("memberId", item.getMember().getId());
    return "redirect:/member/myItems/{memberId}";
  }

  // 내 주문 정보
  @GetMapping("myOrders/{memberId}")
  public String membersOrders(@PathVariable Long memberId, Model model) {
    Member member = memberService.findOneMember(memberId);
    List<Order> orders = member.getOrders();
    model.addAttribute("member", member);
    model.addAttribute("orders", orders);
    model.addAttribute("statusREQUEST", ItemStatus.REQUEST);

    return "member/myOrders";
  }

  // 회원탈퇴
  @GetMapping("/delete/{memberId}")
  public String deleteMember(@PathVariable Long memberId, HttpServletRequest request) {
    memberService.deleteMember(memberService.findOneMember(memberId));
    HttpSession session = request.getSession();
    if (session != null) {
      session.invalidate();
    }
    log.info("회원탈퇴완료");
    return "redirect:/";
  }

}
