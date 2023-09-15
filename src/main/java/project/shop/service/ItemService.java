package project.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.domain.*;
import project.shop.repository.item.ItemRepository;
import project.shop.repository.item.apiquery.ItemQueryRepository;
import project.shop.web.dto.ItemForm;
import project.shop.web.dto.ItemSearchCondition;
import project.shop.web.dto.ItemUpdateForm;
import project.shop.web.dto.SortCondition;

import java.io.IOException;
import java.util.List;

import static project.shop.domain.Item.createItem;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

  private final ItemRepository itemRepository;
  private final ItemQueryRepository itemQueryRepository;
  private final FormattingService formattingService;
  private final FileService fileService;


  @Transactional
  public Item addItem(Member member, ItemForm itemForm) throws IOException {
    // 아이템 생성 후 저장
    Item item = createItem(itemForm.getItemName(), itemForm.getPrice(), itemForm.getQuantity(),
            itemForm.getDescription(), itemForm.getSalesRegion());
    String[] numbers = formattingService.formattingNumber(item.getPrice(), item.getQuantity());
    item.setFormattedNumber(numbers[0], numbers[1]);
    item.assignMember(member);

    // ItemForm 에 이미지가 있다면 저장, 없으면 넘어감
    List<UploadFile> images = fileService.createUploadFileList(itemForm.getImages());
    if (!(images.contains(null))) {
      for (UploadFile image : images) {
        item.setImages(image);
      }
    }
    itemRepository.save(item);
    return item;
  }

  public Item findOneItem(Long itemId) {
    return itemRepository.findById(itemId).orElse(null);
  }

  public Page<Item> findAllByCondition(ItemSearchCondition itemSearchCondition, SortCondition sortCondition, Pageable pageable) {
    return itemRepository.findAllByCondition(itemSearchCondition, sortCondition, pageable);
  }

  // API 전용 조회 메서드
  public List<Item> findAllItemForApi() {
    return itemQueryRepository.findAllItemForApi();
  }

  // 아이템 수정
  @Transactional
  public void updateItem(Long itemId, ItemUpdateForm itemUpdateForm) throws IOException {
    itemRepository.findById(itemId).ifPresent(i -> i.updateItem(
            itemUpdateForm.getItemName(), itemUpdateForm.getPrice(), itemUpdateForm.getQuantity(),
            itemUpdateForm.getDescription(), itemUpdateForm.getRegion()));

    String[] numbers = formattingService.formattingNumber(itemUpdateForm.getPrice(), itemUpdateForm.getQuantity());
    formattingService.setFormattingNumber(itemId, numbers[0], numbers[1]);

    if (itemUpdateForm.getImages() == null || itemUpdateForm.getImages().get(0).isEmpty()) {
      return;
    }

    List<UploadFile> images = fileService.createUploadFileList(itemUpdateForm.getImages());
    for (UploadFile image : images) {
      itemRepository.findById(itemId).ifPresent(m->m.setImages(image));
    }

  }

  @Transactional
  public void changeItemStatus(Long itemId, ItemStatus itemStatus) {
    itemRepository.findById(itemId).ifPresent(i -> i.changeItemStatus(itemStatus));
  }

  @Transactional
  public void assignMember(Long itemId, Member member) {
    itemRepository.findById(itemId).ifPresent(i -> i.assignMember(member));
  }

  @Transactional
  public void assignOrder(Long itemId, Order order) {
    itemRepository.findById(itemId).ifPresent(i -> i.assignOrder(order));
  }

  @Transactional
  public void dismissOrder(Long itemId) {
    itemRepository.findById(itemId).ifPresent(Item::dismissOrder);
  }

  @Transactional
  public void deleteItem(Long itemId) {
    itemRepository.deleteById(itemId);
  }

}
