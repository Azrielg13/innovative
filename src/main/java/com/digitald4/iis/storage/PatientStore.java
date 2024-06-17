package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.Store;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;

import java.util.Objects;
import java.util.stream.Stream;
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
  protected Iterable<Patient> preprocess(Iterable<Patient> patients, boolean isCreate) {
    ImmutableMap<Long, String> vendorNames = vendorStore
        .get(stream(patients).map(Patient::getBillingVendorId).filter(Objects::nonNull).collect(toImmutableSet()))
        .getItems().stream()
        .filter(vendor -> Objects.nonNull(vendor.getName()))
        .collect(toImmutableMap(Vendor::getId, Vendor::getName));

    patients.forEach(p -> p.setBillingVendorName(vendorNames.get(p.getBillingVendorId())));

    return patients;
  }
}
