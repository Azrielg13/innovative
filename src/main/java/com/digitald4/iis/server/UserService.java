package com.digitald4.iis.server;

import com.digitald4.common.model.PasswordInfo;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.UserStore;
import com.digitald4.iis.model.User;
import javax.inject.Inject;
import java.time.Clock;

public class UserService extends com.digitald4.common.server.service.UserService<User> {
  @Inject
  public UserService(
      UserStore<User> userStore, SessionStore<User> sessionStore, Store<PasswordInfo> passwordInfoStore, Clock clock) {
    super(userStore, sessionStore, passwordInfoStore, clock);
  }
}
