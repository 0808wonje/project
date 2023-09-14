package project.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import project.shop.domain.Address;
import project.shop.domain.Item;
import project.shop.domain.Member;
import project.shop.domain.Region;
import project.shop.repository.item.ItemRepository;
import project.shop.repository.member.MemberRepository;
import project.shop.service.FormattingService;
import project.shop.service.ItemService;
import project.shop.service.MemberService;

import javax.annotation.PostConstruct;

import static project.shop.domain.Item.createItem;
import static project.shop.domain.Member.*;

// 초기화 클래스
@Component
@RequiredArgsConstructor
@Profile("local")
public class TestInit {

  private final MemberRepository memberRepository;
  private final ItemRepository itemRepository;
  private final FormattingService formattingService;

  @PostConstruct
  public void init() {

    Member member1 = createMember("userA", "고객1", "1234", new Address(Region.서울특별시, "강남구 청담동"), "010-1234-5678");
    Member member2 = createMember("userB", "고객2", "5678", new Address(Region.경기도, "성남시 분당구 야탑동"), "010-4321-8765");

    memberRepository.save(member1);
    memberRepository.save(member2);

//    Item item1 = createItem("전쟁과 평화", 15000, 3, "톨스토이", Region.경기도);
//    Item item2 = createItem("죄와 벌", 15000, 2, "도스토예프스키", Region.서울특별시);
//    Item item3 = createItem("호밀밭의 파수꾼", 13000, 1, "J.D.샐린저", Region.대구광역시);
//    Item item4 = createItem("노인과 바다", 15000, 3, "헤밍웨이", Region.대전광역시);
//    Item item5 = createItem("데미안", 17000, 3, "헤르만 헤세", Region.부산광역시);
//    Item item6 = createItem("1984", 15000, 1, "조지 오웰", Region.충청북도);
//    Item item7 = createItem("동물농장", 14000, 1, "조지 오웰", Region.강원도);
//    Item item8 = createItem("오만과 편견", 17000, 2, "제인 오스틴", Region.세종특별시);
//    Item item9 = createItem("인간실격", 12000, 5, "다자이 오사무", Region.광주광역시);
//    Item item10 = createItem("이방인", 13000, 4, "알베르 카뮈", Region.경상북도);
//    Item item11 = createItem("폭풍의 언덕", 14000, 1, "에밀리 블론테", Region.충청남도);
//    Item item12 = createItem("위대한 개츠비", 13000, 1, "스콧 F 제럴드", Region.서울특별시);
//    Item item13 = createItem("톰 소여의 모험", 13000, 2, "마크 트웨인", Region.인천광역시);
//    Item item14 = createItem("삼국지", 13000, 9, "나관중", Region.울산광역시);
//    Item item15 = createItem("서울, 1964년 겨울", 13000, 3, "김승옥", Region.인천광역시);
//    Item item16 = createItem("노르웨이의 숲", 13000, 3, "무라카미 하루키", Region.전라남도);
//    Item item17 = createItem("맥주와 케이크", 13000, 3, "서머싯 몸", Region.전라북도);
//    Item item18 = createItem("파우스트", 13000, 3, "괴테", Region.서울특별시);
//    Item item19 = createItem("리어왕", 10000, 4, "윌리엄 셰익스피어", Region.인천광역시);
//    Item item20 = createItem("허클베리 핀의 모험", 10000, 1, "마크 트웨인", Region.울산광역시);
//    Item item21 = createItem("위대한 유산", 11000, 1, "찰스 디킨스", Region.광주광역시);
//    Item item22 = createItem("수레바퀴 아래서", 12000, 2, "헤르만 헤세", Region.세종특별시);
//    Item item23 = createItem("참을 수 없는 존재의 가벼움", 12000, 6, "밀란 쿤데라", Region.경기도);
//    Item item24 = createItem("제인 에어", 11000, 4, "샬롯 브론테", Region.경상북도);
//    Item item25 = createItem("안나 카레리나", 10000, 6, "톨스토이", Region.전라남도);
//
//    String[] numbers1 = formattingService.formattingNumber(item1.getPrice(), item1.getQuantity());
//    String[] numbers2 = formattingService.formattingNumber(item2.getPrice(), item2.getQuantity());
//    String[] numbers3 = formattingService.formattingNumber(item3.getPrice(), item3.getQuantity());
//    String[] numbers4 = formattingService.formattingNumber(item4.getPrice(), item4.getQuantity());
//    String[] numbers5 = formattingService.formattingNumber(item5.getPrice(), item5.getQuantity());
//    String[] numbers6 = formattingService.formattingNumber(item6.getPrice(), item6.getQuantity());
//    String[] numbers7 = formattingService.formattingNumber(item7.getPrice(), item7.getQuantity());
//    String[] numbers8 = formattingService.formattingNumber(item8.getPrice(), item8.getQuantity());
//    String[] numbers9 = formattingService.formattingNumber(item9.getPrice(), item9.getQuantity());
//    String[] numbers10 = formattingService.formattingNumber(item10.getPrice(), item10.getQuantity());
//    String[] numbers11 = formattingService.formattingNumber(item11.getPrice(), item11.getQuantity());
//    String[] numbers12 = formattingService.formattingNumber(item12.getPrice(), item12.getQuantity());
//    String[] numbers13 = formattingService.formattingNumber(item13.getPrice(), item13.getQuantity());
//    String[] numbers14 = formattingService.formattingNumber(item14.getPrice(), item14.getQuantity());
//    String[] numbers15 = formattingService.formattingNumber(item15.getPrice(), item15.getQuantity());
//    String[] numbers16 = formattingService.formattingNumber(item16.getPrice(), item16.getQuantity());
//    String[] numbers17 = formattingService.formattingNumber(item17.getPrice(), item17.getQuantity());
//    String[] numbers18 = formattingService.formattingNumber(item18.getPrice(), item18.getQuantity());
//    String[] numbers19 = formattingService.formattingNumber(item19.getPrice(), item19.getQuantity());
//    String[] numbers20 = formattingService.formattingNumber(item20.getPrice(), item20.getQuantity());
//    String[] numbers21 = formattingService.formattingNumber(item21.getPrice(), item21.getQuantity());
//    String[] numbers22 = formattingService.formattingNumber(item22.getPrice(), item22.getQuantity());
//    String[] numbers23 = formattingService.formattingNumber(item23.getPrice(), item23.getQuantity());
//    String[] numbers24 = formattingService.formattingNumber(item24.getPrice(), item24.getQuantity());
//    String[] numbers25 = formattingService.formattingNumber(item25.getPrice(), item25.getQuantity());
//
//    item1.setFormattedNumber(numbers1[0],numbers1[1]);
//    item2.setFormattedNumber(numbers2[0],numbers2[1]);
//    item3.setFormattedNumber(numbers3[0],numbers3[1]);
//    item4.setFormattedNumber(numbers4[0],numbers4[1]);
//    item5.setFormattedNumber(numbers5[0],numbers5[1]);
//    item6.setFormattedNumber(numbers6[0],numbers6[1]);
//    item7.setFormattedNumber(numbers7[0],numbers7[1]);
//    item8.setFormattedNumber(numbers8[0],numbers8[1]);
//    item9.setFormattedNumber(numbers9[0],numbers9[1]);
//    item10.setFormattedNumber(numbers10[0],numbers10[1]);
//    item11.setFormattedNumber(numbers11[0],numbers11[1]);
//    item12.setFormattedNumber(numbers12[0],numbers12[1]);
//    item13.setFormattedNumber(numbers13[0],numbers13[1]);
//    item14.setFormattedNumber(numbers14[0],numbers14[1]);
//    item15.setFormattedNumber(numbers15[0],numbers15[1]);
//    item16.setFormattedNumber(numbers16[0],numbers16[1]);
//    item17.setFormattedNumber(numbers17[0],numbers17[1]);
//    item18.setFormattedNumber(numbers18[0],numbers18[1]);
//    item19.setFormattedNumber(numbers19[0],numbers19[1]);
//    item20.setFormattedNumber(numbers20[0],numbers20[1]);
//    item21.setFormattedNumber(numbers21[0],numbers21[1]);
//    item22.setFormattedNumber(numbers22[0],numbers22[1]);
//    item23.setFormattedNumber(numbers23[0],numbers23[1]);
//    item24.setFormattedNumber(numbers24[0],numbers24[1]);
//    item25.setFormattedNumber(numbers25[0],numbers25[1]);
//
//    item1.assignMember(member1);
//    item2.assignMember(member2);
//    item3.assignMember(member1);
//    item4.assignMember(member2);
//    item5.assignMember(member2);
//    item6.assignMember(member2);
//    item7.assignMember(member1);
//    item8.assignMember(member2);
//    item9.assignMember(member1);
//    item10.assignMember(member2);
//    item11.assignMember(member1);
//    item12.assignMember(member1);
//    item13.assignMember(member2);
//    item14.assignMember(member1);
//    item15.assignMember(member2);
//    item16.assignMember(member2);
//    item17.assignMember(member1);
//    item18.assignMember(member2);
//    item19.assignMember(member1);
//    item20.assignMember(member1);
//    item21.assignMember(member2);
//    item22.assignMember(member1);
//    item23.assignMember(member1);
//    item24.assignMember(member1);
//    item25.assignMember(member2);
//
//    itemRepository.save(item1);
//    itemRepository.save(item2);
//    itemRepository.save(item3);
//    itemRepository.save(item4);
//    itemRepository.save(item5);
//    itemRepository.save(item6);
//    itemRepository.save(item7);
//    itemRepository.save(item8);
//    itemRepository.save(item9);
//    itemRepository.save(item10);
//    itemRepository.save(item11);
//    itemRepository.save(item12);
//    itemRepository.save(item13);
//    itemRepository.save(item14);
//    itemRepository.save(item15);
//    itemRepository.save(item16);
//    itemRepository.save(item17);
//    itemRepository.save(item18);
//    itemRepository.save(item19);
//    itemRepository.save(item20);
//    itemRepository.save(item21);
//    itemRepository.save(item22);
//    itemRepository.save(item23);
//    itemRepository.save(item24);
//    itemRepository.save(item25);

    for (int i = 0; i < 300; i++) {
      Item item = createItem("book" + i, i, i, "lol", Region.values()[i%16]);
      String[] number = formattingService.formattingNumber(item.getPrice(), item.getQuantity());
      item.setFormattedNumber(number[0], number[1]);
      Member member =  i%2==0 ?  member1 : member2;
      item.assignMember(member);
      itemRepository.save(item);
    }
  }
}
