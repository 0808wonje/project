package project.shop.domain.exceptions;

public class DuplicateLoginIdException extends RuntimeException {

  public DuplicateLoginIdException() {
  }

  public DuplicateLoginIdException(String message) {
    super(message);
  }

  public DuplicateLoginIdException(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateLoginIdException(Throwable cause) {
    super(cause);
  }
}
