package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.model.ReferralResponse;
import javax.inject.Inject;
import javax.inject.Provider;

public class ReferralResponseStore extends GenericStore<ReferralResponse, String> {
  @Inject
  public ReferralResponseStore(Provider<DAO> daoProvider) {
    super(ReferralResponse.class, daoProvider);
  }
}
