package project.shop.web.dto;

import lombok.Getter;
import lombok.Setter;
import project.shop.domain.Region;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberUpdateForm {
  @NotBlank
  private String nickname;
  @NotBlank
  private String password;
  @NotNull
  private Region district;
  @NotBlank
  private String street;
  private String details;
  @NotBlank
  private String phoneNumber;
}
