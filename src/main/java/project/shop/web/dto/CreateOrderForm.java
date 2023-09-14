package project.shop.web.dto;

import lombok.Getter;
import lombok.Setter;
import project.shop.domain.ShippingMethod;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateOrderForm {

  @NotNull
  private ShippingMethod shippingMethod;
}
