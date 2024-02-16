package com.digitald4.iis.server;

import com.digitald4.common.model.ModelObject;
import com.digitald4.common.server.service.EntityServiceBulkImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Store;

public class AdminService<T extends ModelObject<Long>> extends EntityServiceBulkImpl<Long, T> {
  public AdminService(Store<T, Long> store, LoginResolver loginResolver) {
    super(store, loginResolver, true);
  }
}
