package project.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.domain.Address;
import project.shop.domain.exceptions.DuplicateLoginIdException;
import project.shop.domain.Member;
import project.shop.domain.exceptions.LoginFailException;
import project.shop.repository.member.MemberRepository;
import project.shop.web.dto.MemberForm;
import project.shop.web.dto.MemberUpdateForm;

import java.util.List;
import java.util.Optional;

import static project.shop.domain.Member.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public Member joinNewMember(MemberForm memberForm) {
    Address address = new Address(memberForm.getDistrict(), memberForm.getStreet(), memberForm.getDetails());
    Member member = createMember(memberForm.getLoginId(), memberForm.getNickname(), memberForm.getPassword(), address, memberForm.getPhoneNumber());
    memberRepository.save(member);
    return member;
  }

  public Member findOneMember(Long memberId) {
    return memberRepository.findById(memberId).orElse(null);
  }

  public List<Member> findAllMember() {
    return memberRepository.findAll();
  }

  // 로그인
  public Member login(String loginId, String password) {
    Optional<Member> member = memberRepository.findByLoginId(loginId).filter(m -> m.getPassword().equals(password));
    if (member.isPresent()) {
      return member.get();
    } else {
      throw new LoginFailException("아이디나 비밀번호가 맞지않습니다");
    }
  }

  // 중복 loginId 검사
  public void checkDuplicateLoginId(String loginId) {
    memberRepository.findByLoginId(loginId).ifPresent(
      member -> {
        throw new DuplicateLoginIdException("이미 존재하는 아이디입니다");
      }
    );
  }

  // 멤버 수정
  @Transactional
  public void updateMember(Long memberId, MemberUpdateForm memberUpdateForm) {
    memberRepository.findById(memberId).ifPresent(m -> {
      Address address = new Address(memberUpdateForm.getDistrict(), memberUpdateForm.getStreet(), memberUpdateForm.getDetails());
      m.updateMember(memberUpdateForm.getNickname(), memberUpdateForm.getPassword(), address, memberUpdateForm.getPhoneNumber());
    });
  }

  // 멤버 삭제
  @Transactional
  public void deleteMember(Member member) {
    memberRepository.deleteById(member.getId());
  }

}
