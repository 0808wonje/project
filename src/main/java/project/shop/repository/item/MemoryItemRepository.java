package project.shop.repository.item;

import project.shop.domain.Item;
import project.shop.web.dto.ItemSearchCondition;

import java.util.*;

//@Repository
public class MemoryItemRepository {
  private static final Map<Long, Item> itemStore = new HashMap<>();
  private static Long sequence = 100l;

  public void saveItem(Item item) {
    item.assignId(++sequence);
    itemStore.put(item.getId(), item);
  }

  public Optional<Item> findItemById(Long id) {
    return Optional.ofNullable(itemStore.get(id));
  }

  public List<Item> findAllItem(ItemSearchCondition itemSearchCondition) {
    return new ArrayList<>(itemStore.values());
  }

  public void deleteItem(Long id) {
    itemStore.remove(id);
  }

}
