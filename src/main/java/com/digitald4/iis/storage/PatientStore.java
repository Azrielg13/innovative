package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.Store;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;

public class PatientStore extends GenericLongStore<Patient> {

  private final Store<Vendor, Long> vendorStore;

  @Inject
  public PatientStore(Provider<DAO> daoProvider, Store<Vendor, Long> vendorStore) {
    super(Patient.class, daoProvider);
    this.vendorStore = vendorStore;
  }

  @Override
  protected Iterable<Patient> preprocess(Iterable<Patient> entities, boolean isCreate) {
    Map<Long, Vendor> vendors = new HashMap<>();
    return super.preprocess(stream(entities)
        .map(
            p -> p
                .setReferralSourceName(
                    vendors.computeIfAbsent(p.getReferralSourceId(), vendorStore::get).getName())
                .setBillingVendorName(
                    vendors.computeIfAbsent(p.getBillingVendorId(), vendorStore::get).getName()))
        .collect(toImmutableList()), isCreate);
  }
}
