package project.shop.repository.item;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import project.shop.domain.Item;
import project.shop.domain.ItemStatus;
import project.shop.domain.Region;
import project.shop.web.dto.ItemSearchCondition;
import project.shop.web.dto.SortCondition;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static project.shop.domain.QItem.item;
import static project.shop.domain.QMember.member;
import static project.shop.domain.QUploadFile.uploadFile;

@RequiredArgsConstructor
public class ItemCustomRepositoryImpl implements ItemCustomRepository{

  private final JPAQueryFactory query;

  @Override
  public Page<Item> findAllByCondition(ItemSearchCondition searchCondition, SortCondition sortCondition, Pageable pageable) {
    String searchItemName = searchCondition.getSearchItemName();
    Integer minPrice = searchCondition.getMinPrice();
    Integer maxPrice = searchCondition.getMaxPrice();
    String searchUserNickname = searchCondition.getSearchUserNickname();
    Region region = searchCondition.getDistrict();
    List<Item> items = query.select(item)
            .from(item)
            .join(item.member, member).leftJoin(item.images, uploadFile).fetchJoin()
            .where(
                    likeItemName(searchItemName),
                    minPrice(minPrice),
                    maxPrice(maxPrice),
                    likeUserNickname(searchUserNickname),
                    likeRegion(region),
                    onSaleItem()
            )
            .orderBy(
                    priceHighToLow(sortCondition.getPriceHighToLow()),
                    priceLowToHigh(sortCondition.getPriceLowToHigh()),
                    newest(sortCondition.getNewest()),
                    hasMostComment(sortCondition.getMostComment())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPAQuery<Long> getTotal = query
            .select(item.count())
            .from(item)
            .join(item.member, member).leftJoin(item.images, uploadFile)
            .where(likeItemName(searchItemName),
                    minPrice(minPrice), maxPrice(maxPrice),
                    likeUserNickname(searchUserNickname),
                    likeRegion(region),
                    onSaleItem());

    return PageableExecutionUtils.getPage(items, pageable, getTotal::fetchOne);

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

  private OrderSpecifier priceHighToLow(Boolean priceHighToLow) {
    if (priceHighToLow) {
      return new OrderSpecifier(Order.DESC, item.price, OrderSpecifier.NullHandling.Default);
    }
    return new OrderSpecifier(Order.DESC, NullExpression.DEFAULT);
  }

  private OrderSpecifier priceLowToHigh(Boolean priceLowToHigh) {
    if (priceLowToHigh) {
      return new OrderSpecifier(Order.ASC, item.price);
    }
    return new OrderSpecifier(Order.DESC, NullExpression.DEFAULT);
  }
  private OrderSpecifier newest(Boolean newest) {
    if (newest) {
      return new OrderSpecifier(Order.DESC, item.createdTime);
    }
    return new OrderSpecifier(Order.DESC, NullExpression.DEFAULT);
  }

  private OrderSpecifier hasMostComment(Boolean mostComment) {
    if (mostComment) {
      return new OrderSpecifier(Order.DESC, item.comments.size());
    }
    return new OrderSpecifier(Order.DESC, NullExpression.DEFAULT);
  }



}
