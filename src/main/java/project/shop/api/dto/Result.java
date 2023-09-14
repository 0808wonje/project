package project.shop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Result<T> {
  private int allCnt;
  private T dtoList;

}
