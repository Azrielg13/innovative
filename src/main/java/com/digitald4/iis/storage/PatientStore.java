package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
import com.google.common.collect.ImmutableMap;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

public class PatientStore extends GenericLongStore<Patient> {
  private final Provider<DAO> daoProvider;

  @Inject
  public PatientStore(Provider<DAO> daoProvider) {
    super(Patient.class, daoProvider);
    this.daoProvider = daoProvider;
  }

  @Override
  protected Iterable<Patient> preprocess(Iterable<Patient> patients, boolean isCreate) {
    ImmutableMap<Long, String> vendorNames = daoProvider.get()
        .get(Vendor.class, stream(patients).map(Patient::getBillingVendorId).filter(Objects::nonNull).collect(toImmutableSet()))
        .getItems().stream()
        .filter(vendor -> Objects.nonNull(vendor.getName()))
        .collect(toImmutableMap(Vendor::getId, Vendor::getName));

    patients.forEach(p -> p.setBillingVendorName(vendorNames.get(p.getBillingVendorId())));

    return patients;
  }
}
