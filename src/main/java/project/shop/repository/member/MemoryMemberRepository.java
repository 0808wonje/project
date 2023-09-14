package project.shop.repository.member;

import lombok.extern.slf4j.Slf4j;
import project.shop.domain.Member;

import java.util.*;

//@Repository
@Slf4j
public class MemoryMemberRepository {
  private static final Map<Long, Member> store = new HashMap<>();
  private static Long sequence = 0l;

  public void saveMember(Member member) throws IllegalStateException {
    checkDuplicateLoginId(member.getLoginId());
    member.assignId(++sequence);
    store.put(member.getId(), member);
  }

  public Optional<Member> findMemberById(Long id) {
    Member member = store.get(id);
    return Optional.ofNullable(member);
  }

  public Optional<Member> findMemberByLoginId(String loginId) {
    Optional<Member> member = store.values().stream().filter(m -> m.getLoginId().equals(loginId)).findAny();
    return member;
  }

  public void deleteMember(Long id) {
    store.remove(id);
  }

  public List<Member> findAllMember() {
    return new ArrayList<>(store.values());
  }


  public void checkDuplicateLoginId(String loginId) {
    store.values().stream().filter(member -> member.getLoginId().equals(loginId)).findAny().
            ifPresent(member -> {
              throw new IllegalStateException("이미 존재하는 아이디입니다");
            });
  }
}
