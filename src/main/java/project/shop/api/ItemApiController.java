package project.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.shop.api.dto.ItemResponseDto;
import project.shop.api.dto.Result;
import project.shop.domain.Item;
import project.shop.domain.Member;
import project.shop.service.FormattingService;
import project.shop.service.ItemService;
import project.shop.service.MemberService;
import project.shop.web.dto.ItemForm;
import project.shop.web.dto.ItemUpdateForm;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class ItemApiController {

  private final ItemService itemService;
  private final MemberService memberService;
  private final FormattingService formattingService;

  // 상품 등록 API
  @PostMapping("/api/itemCreate/{memberId}")
  public ItemResponseDto addItem(@PathVariable Long memberId, @RequestBody @Validated ItemForm itemForm) throws IOException {
    // 멤버 조회
    Member member = memberService.findOneMember(memberId);

    // 상품 생성
    Item item = itemService.addItem(member, itemForm);

    return new ItemResponseDto(item);
  }

  // 상품 삭제 API
  @PostMapping("/api/itemDelete/{itemId}")
  public String deleteItem(@PathVariable Long itemId) {
    itemService.deleteItem(itemId);
    return "Successfully Deleted";
  }

  // 상품 수정 API
  @PostMapping("/api/itemUpdate/{itemId}")
  public ItemResponseDto updateItem(@PathVariable Long itemId, @RequestBody @Validated ItemUpdateForm itemUpdateForm) throws IOException {
    // 상품 조회
    Item item = itemService.findOneItem(itemId);

    // 상품 수정
    itemService.updateItem(itemId, itemUpdateForm);
    String[] numbers = formattingService.formattingNumber(itemUpdateForm.getPrice(), itemUpdateForm.getQuantity());
    item.setFormattedNumber(numbers[0], numbers[1]);

    return new ItemResponseDto(item);
  }

  // 모든 상품 조회 API
  @GetMapping("/api/allItem")
  public Result getAllItem() {
    List<Item> allItem = itemService.findAllItemForApi();
    List<ItemResponseDto> itemResponseDtos = allItem.stream().map(ItemResponseDto::new).collect(toList());
    return new Result(itemResponseDtos.size(), itemResponseDtos);
  }
}
