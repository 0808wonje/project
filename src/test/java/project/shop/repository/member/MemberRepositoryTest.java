package project.shop.repository.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.shop.domain.Address;
import project.shop.domain.Member;
import project.shop.domain.Region;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static project.shop.domain.Member.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Test
  void saveAndFindTest() {
    Member member = createMember("test", "dummy", "1111", new Address(Region.대구광역시, "수정구"), "010-1234-5678");
    Member saved = memberRepository.save(member);

    List<Member> all = memberRepository.findAll();

    Assertions.assertThat(saved).isEqualTo(member);
    Assertions.assertThat(all.size()).isEqualTo(1);
  }

}