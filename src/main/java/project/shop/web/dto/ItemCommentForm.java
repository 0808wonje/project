package project.shop.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ItemCommentForm {

  @NotBlank
  private String comment;
}
