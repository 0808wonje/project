package project.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByLoginId(String loginId);
}
