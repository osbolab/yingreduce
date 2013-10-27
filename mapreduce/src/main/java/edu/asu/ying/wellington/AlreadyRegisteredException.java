package edu.asu.ying.wellington;

import edu.asu.ying.wellington.service.Service;
import edu.asu.ying.wellington.service.ServiceException;

/**
 * Thrown when an instance attempts to register with the service locator where that name is already
 * bound to an instance of that class.
 */
public class AlreadyRegisteredException extends ServiceException {

  public AlreadyRegisteredException(String name, Class<? extends Service> cls) {
    super(String.format("An instance of class %s named `%s` is already registered.",
                        cls.getName(), name));
  }
}
