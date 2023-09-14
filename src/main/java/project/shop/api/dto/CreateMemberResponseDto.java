package project.shop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMemberResponseDto {

  private Long id;
  private String loginId;
  private String password;

}
