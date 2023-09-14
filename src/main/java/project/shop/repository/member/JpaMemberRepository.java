package project.shop.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.shop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaMemberRepository {

  private final EntityManager em;

  public void saveMember(Member member) {
    em.persist(member);
    log.info("멤버 저장성공");
  }

  public Optional<Member> findMemberById(Long id) {
    Member member = em.find(Member.class, id);
    return Optional.ofNullable(member);
  }

  public Optional<Member> findMemberByLoginId(String loginId) {
    String jpql = "select m from Member m where m.loginId = :loginId";
    try {
      Member member = em.createQuery(jpql, Member.class).setParameter("loginId", loginId).getSingleResult();
      return Optional.of(member);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public void deleteMember(Long id) {
    Optional<Member> member = findMemberById(id);
    if (member.isPresent()) {
      em.remove(member.get());
    }
  }

  public List<Member> findAllMember() {
    String jpql = "select m from Member m";
    return em.createQuery(jpql, Member.class).getResultList();
  }


}
