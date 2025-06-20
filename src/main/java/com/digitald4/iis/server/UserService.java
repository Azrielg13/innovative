package com.digitald4.iis.server;

import com.digitald4.common.storage.PasswordStore;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.iis.model.User;
import com.digitald4.iis.storage.UserStore;
import javax.inject.Inject;

public class UserService extends com.digitald4.common.server.service.UserService<User> {
  @Inject
  public UserService(UserStore userStore, SessionStore<User> sessionStore, PasswordStore passwordStore) {
    super(userStore, sessionStore, passwordStore);
  }
}
