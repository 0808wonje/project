package project.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.shop.domain.*;
import project.shop.repository.comment.CommentRepository;
import project.shop.service.*;
import project.shop.web.dto.CreateOrderForm;
import project.shop.web.dto.ItemCommentForm;
import project.shop.web.dto.ItemForm;
import project.shop.web.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;

import static project.shop.domain.Comment.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;
  private final FileService fileService;
  private final MemberService memberService;
  private final CommentRepository commentRepository;

  // 상품등록 폼으로 이동
  @GetMapping("item/additem")
  public String itemForm(@ModelAttribute ItemForm itemForm, Model model, HttpServletRequest request) {
    model.addAttribute("member", request.getSession().getAttribute(SessionConst.LOGIN_MEMBER));
    model.addAttribute("regions", Region.values());
    return "item/addItemForm";
  }

  // 상품등록
  @PostMapping("item/additem")
  public String addItem(@Validated @ModelAttribute ItemForm itemForm, BindingResult bindingResult,
                        HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) throws IOException {
    Long memberId = (Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER_ID);
    Member member = memberService.findOneMember(memberId);

    // ItemForm 확인
    if (bindingResult.hasErrors()) {
      model.addAttribute("member", member);
      model.addAttribute("regions", Region.values());
      log.info("error = {}", bindingResult);
      return "item/addItemForm";
    }

    Item item = itemService.addItem(member, itemForm);

    redirectAttributes.addAttribute("itemId", item.getId());
    log.info("아이템 저장 성공 item images = {}", item.getImages());
    return "redirect:/item/itemInfo/{itemId}";
  }

  // 해당 아이템 페이지로 이동
  @GetMapping("/item/itemInfo/{itemId}")
  public String showItemInfo(@PathVariable Long itemId,
                             @ModelAttribute ItemCommentForm itemCommentForm, @ModelAttribute CreateOrderForm createOrderForm,
                             Model model, HttpSession session) {
    Item item = itemService.findOneItem(itemId);
    Member client = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

    if (client == null) {
      model.addAttribute("item", item);
      model.addAttribute("shippingWays", ShippingMethod.values());
      return "item/itemInfo";
    }

    Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER_ID);
    Member member = memberService.findOneMember(memberId);
    model.addAttribute("item", item);
    model.addAttribute("member", member);
    model.addAttribute("shippingWays", ShippingMethod.values());
    return "item/itemInfo";
  }

  // 아이템 페이지에서 이미지 렌더링
  @ResponseBody
  @GetMapping("/images/{filename}")
  public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
    return new UrlResource("file:" + fileService.getFullPath(filename));
  }

  // 댓글등록
  @PostMapping("item/addComment/{itemId}")
  public String addComment(@PathVariable Long itemId,
                           @Validated @ModelAttribute ItemCommentForm itemCommentForm, BindingResult bindingResult1,
                           @ModelAttribute CreateOrderForm createOrderForm, BindingResult bindingResult2,
                           Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

    Long memberId = (Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER_ID);
    Member member = memberService.findOneMember(memberId);
    Item item = itemService.findOneItem(itemId);

    // itemCommentForm 확인
    if (bindingResult1.hasErrors()) {
      log.info("error = {}", bindingResult1);
      model.addAttribute("member", member);
      model.addAttribute("item", item);
      model.addAttribute("shippingWays", ShippingMethod.values());
      return "item/itemInfo";
    }

    // 댓글 생성 후 아이템 화면으로 리다이렉트
    Comment comment = createComment(itemCommentForm.getComment(), member, item);
    comment.assignMemberAndItem(member, item);
    commentRepository.save(comment);

    redirectAttributes.addAttribute("itemId", itemId);
    return "redirect:/item/itemInfo/{itemId}";
  }
}
