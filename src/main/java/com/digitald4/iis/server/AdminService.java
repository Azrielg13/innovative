package com.digitald4.iis.server;

import com.digitald4.common.server.service.EntityServiceImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Store;

public class AdminService<T> extends EntityServiceImpl<T, Long> {
  public AdminService(Store<T, Long> store, LoginResolver loginResolver) {
    super(store, loginResolver, true);
  }
}
