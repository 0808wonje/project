package project.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.shop.domain.*;
import project.shop.domain.exceptions.DuplicateLoginIdException;
import project.shop.domain.exceptions.LoginFailException;
import project.shop.service.ItemService;
import project.shop.service.MemberService;
import project.shop.web.dto.*;
import project.shop.web.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

  private final MemberService memberService;
  private final ItemService itemService;

  // 메인화면
  @GetMapping("/")
  public String goHome(@ModelAttribute ItemSearchCondition itemSearchCondition,
                       @RequestParam(defaultValue = "newest") String sortParam,
                       HttpServletRequest request, Model model, Pageable pageable) {
    Page<Item> page = itemService.findAllByCondition(itemSearchCondition, new SortCondition(sortParam), pageable);

    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
      model.addAttribute("allItem", page.getContent());
      model.addAttribute("regions", Region.values());
      model.addAttribute("page", page);
      model.addAttribute("curPageNum", page.getNumber());
      model.addAttribute("sParam", sortParam);
      return "itemlist";
    }

    Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER_ID);
    Member member = memberService.findOneMember(memberId);
    model.addAttribute("member", member);
    model.addAttribute("allItem", page.getContent());
    model.addAttribute("regions", Region.values());
    model.addAttribute("page", page);
    model.addAttribute("curPageNum", page.getNumber());
    model.addAttribute("sParam", sortParam);
    return "itemlist";
  }

  // 로그인 페이지 이동
  @GetMapping("/login")
  public String goLoginPage(@ModelAttribute LoginForm loginForm) {
    return "login";
  }

  // 로그인
  @PostMapping("/login")
  public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request,
                      @RequestParam(defaultValue = "/") String redirectURL, RedirectAttributes redirectAttributes) {
    // LoginForm 확인
    if (bindingResult.hasErrors()) {
      return "login";
    }

    // 로그인 성공
    try {
      Member member = memberService.login(loginForm.getLoginId(), loginForm.getPassword());
      request.getSession().setAttribute(SessionConst.LOGIN_MEMBER, member);
      request.getSession().setAttribute(SessionConst.LOGIN_MEMBER_ID, member.getId());
      redirectAttributes.addAttribute("memberId", member.getLoginId());
      return "redirect:" + redirectURL;
    } // 로그인 실패
    catch (LoginFailException e) {
      bindingResult.reject("loginFail", "아이디나 비밀번호가 맞지않습니다");
      return "login";
    }
  }

  // 회원가입 페이지 이동
  @GetMapping("/join")
  public String goJoinPage(@ModelAttribute MemberForm memberForm, Model model) {
    model.addAttribute("regions", Region.values());
    return "join";
  }

  // 회원가입
  @PostMapping("/join")
  public String join(@Validated @ModelAttribute MemberForm memberForm, BindingResult bindingResult,
                     RedirectAttributes redirectAttributes, Model model) {

    // MemberForm 확인
    if (bindingResult.hasErrors()) {
      model.addAttribute("regions", Region.values());
      return "join";
    }

    // 중복 아이디 확인
    try {
      memberService.checkDuplicateLoginId(memberForm.getLoginId());
    } catch (DuplicateLoginIdException e) {
      model.addAttribute("regions", Region.values());
      bindingResult.reject("duplicateLoginId",e.getMessage());
      return "join";
    }

    // 멤버 생성 후 저장
    memberService.joinNewMember(memberForm);

    redirectAttributes.addAttribute("complete", true);
    return "redirect:/login";
  }

  // 로그아웃
  @GetMapping("/logout")
  public String logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    log.info("로그아웃 성공");
    return "redirect:/";
  }
}
