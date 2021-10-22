package com.digitald4.iis.server;

import com.digitald4.common.server.service.EntityServiceImpl;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.common.storage.Store;
import com.digitald4.iis.model.User;

public class AdminService<T> extends EntityServiceImpl<T> {
  public AdminService(Store<T> store, SessionStore<User> sessionStore) {
    super(store, sessionStore, true);
  }
}
