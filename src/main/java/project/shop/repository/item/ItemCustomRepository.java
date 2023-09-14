package project.shop.repository.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.shop.domain.Item;
import project.shop.web.dto.ItemSearchCondition;
import project.shop.web.dto.SortCondition;


public interface ItemCustomRepository {

  Page<Item> findAllByCondition(ItemSearchCondition searchCondition, SortCondition sortCondition, Pageable pageable);
}
