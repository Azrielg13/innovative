package com.digitald4.iis.server;

import com.digitald4.common.storage.PasswordStore;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.common.storage.UserStore;
import com.digitald4.iis.model.User;
import javax.inject.Inject;

public class UserService extends com.digitald4.common.server.service.UserService<User> {
  @Inject
  public UserService(
      UserStore<User> userStore, SessionStore<User> sessionStore, PasswordStore passwordStore) {
    super(userStore, sessionStore, passwordStore);
  }
}
