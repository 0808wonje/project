package project.shop.domain;

public enum ShippingMethod {

  PARCEL("택배"), DIRECT("직거래");

  private final String label;

  ShippingMethod(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
