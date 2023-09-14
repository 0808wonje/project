package project.shop.domain.exceptions;

public class LoginFailException extends RuntimeException {
  public LoginFailException() {
  }

  public LoginFailException(String message) {
    super(message);
  }

  public LoginFailException(String message, Throwable cause) {
    super(message, cause);
  }

  public LoginFailException(Throwable cause) {
    super(cause);
  }
}
