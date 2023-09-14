package project.shop.repository.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import project.shop.domain.Address;
import project.shop.domain.Item;
import project.shop.domain.Member;
import project.shop.domain.Region;
import project.shop.repository.member.MemberRepository;
import project.shop.service.FormattingService;
import project.shop.web.dto.ItemSearchCondition;
import project.shop.web.dto.SortCondition;

import static org.assertj.core.api.Assertions.*;
import static project.shop.domain.Item.createItem;
import static project.shop.domain.Member.createMember;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  FormattingService formattingService;

  @BeforeEach
  void init() {
    Member member1 = createMember("userA", "짱구", "1234", new Address(Region.경기도, "분당구 서현동"), "010-xxxx-xxxx");
    Member member2 = createMember("userB", "철수", "5678", new Address(Region.경기도, "분당구 서현동"), "010-xxxx-xxxx");

    memberRepository.save(member1);
    memberRepository.save(member2);

    Item item1 = createItem("전쟁과 평화", 15000, 3, "톨스토이", Region.경기도);
    Item item2 = createItem("죄와 벌", 15000, 2, "도스토예프스키", Region.서울특별시);
    Item item3 = createItem("호밀밭의 파수꾼", 13000, 1, "J.D.샐린저", Region.대구광역시);
    Item item4 = createItem("노인과 바다", 15000, 3, "헤밍웨이", Region.대전광역시);
    Item item5 = createItem("데미안", 17000, 3, "헤르만 헤세", Region.부산광역시);
    Item item6 = createItem("1984", 15000, 1, "조지 오웰", Region.충청북도);
    Item item7 = createItem("동물농장", 14000, 1, "조지 오웰", Region.강원도);
    Item item8 = createItem("오만과 편견", 17000, 2, "제인 오스틴", Region.세종특별시);
    Item item9 = createItem("인간실격", 12000, 5, "다자이 오사무", Region.광주광역시);
    Item item10 = createItem("이방인", 13000, 4, "알베르 카뮈", Region.경상북도);

    String[] numbers1 = formattingService.formattingNumber(item1.getPrice(), item1.getQuantity());
    String[] numbers2 = formattingService.formattingNumber(item2.getPrice(), item2.getQuantity());
    String[] numbers3 = formattingService.formattingNumber(item3.getPrice(), item3.getQuantity());
    String[] numbers4 = formattingService.formattingNumber(item4.getPrice(), item4.getQuantity());
    String[] numbers5 = formattingService.formattingNumber(item5.getPrice(), item5.getQuantity());
    String[] numbers6 = formattingService.formattingNumber(item6.getPrice(), item6.getQuantity());
    String[] numbers7 = formattingService.formattingNumber(item7.getPrice(), item7.getQuantity());
    String[] numbers8 = formattingService.formattingNumber(item8.getPrice(), item8.getQuantity());
    String[] numbers9 = formattingService.formattingNumber(item9.getPrice(), item9.getQuantity());
    String[] numbers10 = formattingService.formattingNumber(item10.getPrice(), item10.getQuantity());

    item1.setFormattedNumber(numbers1[0],numbers1[1]);
    item2.setFormattedNumber(numbers2[0],numbers2[1]);
    item3.setFormattedNumber(numbers3[0],numbers3[1]);
    item4.setFormattedNumber(numbers4[0],numbers4[1]);
    item5.setFormattedNumber(numbers5[0],numbers5[1]);
    item6.setFormattedNumber(numbers6[0],numbers6[1]);
    item7.setFormattedNumber(numbers7[0],numbers7[1]);
    item8.setFormattedNumber(numbers8[0],numbers8[1]);
    item9.setFormattedNumber(numbers9[0],numbers9[1]);
    item10.setFormattedNumber(numbers10[0],numbers10[1]);

    item1.assignMember(member1);
    item2.assignMember(member2);
    item3.assignMember(member1);
    item4.assignMember(member2);
    item5.assignMember(member2);
    item6.assignMember(member2);
    item7.assignMember(member1);
    item8.assignMember(member2);
    item9.assignMember(member1);
    item10.assignMember(member2);

    itemRepository.save(item1);
    itemRepository.save(item2);
    itemRepository.save(item3);
    itemRepository.save(item4);
    itemRepository.save(item5);
    itemRepository.save(item6);
    itemRepository.save(item7);
    itemRepository.save(item8);
    itemRepository.save(item9);
    itemRepository.save(item10);
  }

  @Test
  void findAllItemByCondition() {
    ItemSearchCondition itemSearchCondition = new ItemSearchCondition();
    PageRequest pageRequest = PageRequest.of(0, 5);
    SortCondition sortCondition = new SortCondition("newest");
    Page<Item> page = itemRepository.findAllByCondition(itemSearchCondition, sortCondition, pageRequest);

    assertThat(page.getTotalElements()).isEqualTo(10);
    assertThat(page.getSize()).isEqualTo(5);
    assertThat(page.hasNext()).isTrue();
    assertThat(page.getContent()).extracting("itemName").containsExactly("전쟁과 평화", "죄와 벌", "호밀밭의 파수꾼", "노인과 바다", "데미안");
  }

}