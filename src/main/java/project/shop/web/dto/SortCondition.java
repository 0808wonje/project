package project.shop.web.dto;

import lombok.Getter;


@Getter
public class SortCondition {

  private Boolean newest = false;
  private Boolean priceLowToHigh = false;
  private Boolean priceHighToLow = false;
  private Boolean mostComment = false;

  public SortCondition(String sortParam) {
    if (sortParam.equals("newest")) {
      newest = true;
    } else if (sortParam.equals("priceLowToHigh")) {
      priceLowToHigh = true;
    } else if (sortParam.equals("priceHighToLow")) {
      priceHighToLow = true;
    } else if (sortParam.equals("mostComment")) {
      mostComment = true;
    }
  }

}
