package project.shop.web.dto;

import lombok.Getter;
import lombok.Setter;
import project.shop.domain.Region;

@Getter
@Setter
public class ItemSearchCondition {

  private String searchUserNickname;
  private Region district = null;
  private String searchItemName;
  private Integer minPrice;
  private Integer maxPrice;

}
