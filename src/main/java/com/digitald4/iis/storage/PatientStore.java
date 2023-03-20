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
            patient -> patient
                .setReferralSourceName(getVendorName(patient.getReferralSourceId(), vendors))
                .setBillingVendorName(getVendorName(patient.getBillingVendorId(), vendors)))
        .collect(toImmutableList()), isCreate);
  }

  private String getVendorName(Long vendorId, Map<Long, Vendor> vendors) {
    if (vendorId == null || vendorId == 0) {
      return null;
    }

    Vendor vendor = vendors.computeIfAbsent(vendorId, vendorStore::get);
    return vendor == null ? null : vendor.getName();
  }
}
