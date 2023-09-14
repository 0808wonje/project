package project.shop.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.shop.api.dto.ErrorResult;
import project.shop.domain.exceptions.DuplicateLoginIdException;
import project.shop.domain.exceptions.ItemStatusException;
import project.shop.domain.exceptions.LoginFailException;

@RestControllerAdvice
public class ApiControllerAdvice {

  @ExceptionHandler(DuplicateLoginIdException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResult hasDuplicateLoginIdError(DuplicateLoginIdException e) {
    return new ErrorResult("BAD_REQUEST", e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResult hasArgumentError(MethodArgumentNotValidException e) {
    return new ErrorResult("BAD_REQUEST", "Argument Error");
  }

  @ExceptionHandler(LoginFailException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResult hasLoginError(LoginFailException e) {
    return new ErrorResult("BAD_REQUEST", e.getMessage());
  }

  @ExceptionHandler(ItemStatusException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResult hasItemStatusError(ItemStatusException e) {
    return new ErrorResult("BAD_REQUEST", e.getMessage());
  }
}
