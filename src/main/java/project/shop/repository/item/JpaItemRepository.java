package project.shop.repository.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.shop.domain.*;
import project.shop.repository.comment.CommentRepository;
import project.shop.repository.uploadfile.JpaUploadFileRepository;
import project.shop.repository.order.JpaOrderRepository;
import project.shop.web.dto.ItemSearchCondition;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.*;
import static project.shop.domain.QItem.*;
import static project.shop.domain.QMember.*;
import static project.shop.domain.QUploadFile.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JpaItemRepository {

  private final EntityManager em;
  private final JpaUploadFileRepository jpaUploadFileRepository;
  private final JpaOrderRepository jpaOrderRepository;
  private final CommentRepository commentRepository;
  private final JPAQueryFactory query;


  public void saveItem(Item item) {
    em.persist(item);
  }

  public Optional<Item> findItemById(Long id) {
    Item item = em.find(Item.class, id);
    return Optional.ofNullable(item);
  }

  public List<Item> findAllItem(ItemSearchCondition searchCondition) {
    String searchItemName = searchCondition.getSearchItemName();
    Integer minPrice = searchCondition.getMinPrice();
    Integer maxPrice = searchCondition.getMaxPrice();
    String searchUserNickname = searchCondition.getSearchUserNickname();
    Region region = searchCondition.getDistrict();

    return query.select(item)
            .from(item)
            .join(item.member, member).leftJoin(item.images, uploadFile).fetchJoin()
            .where(likeItemName(searchItemName), minPrice(minPrice), maxPrice(maxPrice), likeUserNickname(searchUserNickname), likeRegion(region), onSaleItem())
            .fetch();

  }

  private BooleanExpression likeUserNickname(String searchUserNickname) {
    if (hasText(searchUserNickname)) {
      return item.member.nickname.like("%" + searchUserNickname + "%");
    }
    return null;
  }

  private BooleanExpression likeRegion(Region region) {
    if (region != null) {
      return item.region.eq(region);
    }
    return null;
  }

  private BooleanExpression likeItemName(String searchItemName) {
    if (hasText(searchItemName)) {
      return item.itemName.like("%" + searchItemName + "%");
    }
    return null;
  }

  private BooleanExpression minPrice(Integer minPrice) {
    if (minPrice != null) {
      return item.price.goe(minPrice);
    }
    return null;
  }

  private BooleanExpression maxPrice(Integer maxPrice) {
    if (maxPrice != null) {
      return item.price.loe(maxPrice);
    }
    return null;
  }

  private BooleanExpression onSaleItem() {
    return item.itemStatus.eq(ItemStatus.ONSALE);
  }


  public void deleteItem(Long id) {
    Optional<Item> item = findItemById(id);

    // 아이템 이미지 삭제
    if (item.get().getImages().size() > 0) {
      for (UploadFile uploadFile : item.get().getImages()) {
        jpaUploadFileRepository.deleteUploadFile(uploadFile.getId());
      }
      em.remove(item.get());
      return;
    }

    // 아이템 댓글 삭제
    if (item.get().getComments().size() > 0) {
      for (Comment comment : item.get().getComments()) {
        commentRepository.deleteById(comment.getId());
      }
    }

    // 아이템 주문 삭제
    if (item.get().getOrder() != null) {
      jpaOrderRepository.removeOrder(item.get().getOrder().getId());
    }

    // 아이템 삭제
    em.remove(item.get());
  }
}
