package project.shop.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.shop.api.dto.CreateMemberResponseDto;
import project.shop.api.dto.MemberResponseDto;
import project.shop.api.dto.Result;
import project.shop.domain.Member;
import project.shop.service.MemberService;
import project.shop.web.dto.LoginForm;
import project.shop.web.dto.MemberForm;
import project.shop.web.dto.MemberUpdateForm;

import java.util.List;

import static java.util.stream.Collectors.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

  private final MemberService memberService;

  // 회원가입 API
  @PostMapping("/api/memberCreate")
  public CreateMemberResponseDto createMember(@RequestBody @Validated MemberForm memberForm) {
    memberService.checkDuplicateLoginId(memberForm.getLoginId());
    Member member = memberService.joinNewMember(memberForm);
    return new CreateMemberResponseDto(member.getId(), member.getLoginId(), member.getPassword());
  }

  // 로그인 API
  @PostMapping("/api/login")
  public MemberResponseDto login(@RequestBody @Validated LoginForm loginForm) {
    Member member = memberService.login(loginForm.getLoginId(), loginForm.getPassword());
    return new MemberResponseDto(member);
  }


  // 회원정보 수정 API
  @PostMapping("/api/memberUpdate/{memberId}")
  public MemberResponseDto updateMember(@PathVariable Long memberId, @RequestBody @Validated MemberUpdateForm memberUpdateForm) {
    memberService.updateMember(memberId, memberUpdateForm);
    Member member = memberService.findOneMember(memberId);
    return new MemberResponseDto(member);
  }

  // 회원탈퇴 API
  @PostMapping("/api/memberDelete/{memberId}")
  public String deleteMember(@PathVariable Long memberId) {
    Member member = memberService.findOneMember(memberId);
    memberService.deleteMember(member);
    return "Successfully Deleted";
  }

  // 모든 회원 조회 API (회원들의 상품, 주문 정보 포함하여 출력)
  @GetMapping("/api/allMember")
  public Result getAllMember() {
    List<Member> allMember = memberService.findAllMember();
    List<MemberResponseDto> memberDtoList = allMember.stream().map(MemberResponseDto::new).collect(toList());
    return new Result(memberDtoList.size(), memberDtoList);
  }


}
