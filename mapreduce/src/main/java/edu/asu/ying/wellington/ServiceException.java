package edu.asu.ying.wellington;

/**
 *
 */
public abstract class ServiceException extends Exception {

  public ServiceException() {
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
