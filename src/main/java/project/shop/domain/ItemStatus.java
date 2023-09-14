package project.shop.domain;

public enum ItemStatus {
  ONSALE("판매중"), REQUEST("주문요청"), TRADING("거래중"), COMPLETE("거래완료");

  private final String label;

  ItemStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
