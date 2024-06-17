package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.iis.model.ServiceCode;
import com.digitald4.iis.model.Vendor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Objects;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.Streams.stream;

public class ServiceCodeStore extends GenericStore<ServiceCode, String> {
  private final Provider<DAO> daoProvider;

  @Inject
  public ServiceCodeStore(Provider<DAO> daoProvider) {
    super(ServiceCode.class, daoProvider);
    this.daoProvider = daoProvider;
  }

  @Override
  protected Iterable<ServiceCode> preprocess(Iterable<ServiceCode> serviceCodes, boolean isCreate) {
    super.preprocess(serviceCodes, isCreate);

    ImmutableMap<Long, String> vendorNames = daoProvider.get()
        .get(Vendor.class,
            stream(serviceCodes).map(ServiceCode::getVendorId).filter(Objects::nonNull).collect(toImmutableList()))
        .getItems()
        .stream().collect(toImmutableMap(Vendor::getId, Vendor::toString));

    return stream(serviceCodes)
        .map(serviceCode -> serviceCode.setVendorName(vendorNames.get(serviceCode.getVendorId())))
        .collect(toImmutableList());
  }

  public ServiceCode get(long vendorId, String billCode) {
    return get(String.format("%s-%s", vendorId, billCode));
  }

  public ImmutableList<ServiceCode> get(long vendorId) {
    return list(Query.forList(Filter.of("VendorId", vendorId))).getItems();
  }
}
