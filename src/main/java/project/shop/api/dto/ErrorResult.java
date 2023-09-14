package project.shop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResult {
  private String errorCode;
  private String errorMessage;
}
