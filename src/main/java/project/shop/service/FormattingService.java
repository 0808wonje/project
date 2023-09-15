package project.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.repository.item.ItemRepository;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormattingService {

  private final Formatter<Number> numberFormatter;
  private final ItemRepository itemRepository;

  // 숫자를 문자로 변환함 #,### 형태
  public String[] formattingNumber(Integer price, Integer quantity) {
    log.info("numberFormatter = {}", numberFormatter);
    String printedPrice = numberFormatter.print(price, Locale.KOREA);
    String printedQuantity = numberFormatter.print(quantity, Locale.KOREA);
    return new String[]{printedPrice, printedQuantity};
  }

  // 아이템에 변환한 문자를 세팅
  @Transactional
  public void setFormattingNumber(Long itemId, String price, String quantity) {
    itemRepository.findById(itemId).get().setFormattedNumber(price, quantity);
  }

}
