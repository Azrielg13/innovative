package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import com.google.common.collect.ImmutableMap;
import javax.inject.Inject;
import javax.inject.Provider;

public class LicenseStore extends GenericStore<License, String> {
  private final GeneralDataStore generalDataStore;
  private final NurseStore nurseStore;

  @Inject
  public LicenseStore(Provider<DAO> daoProvider, GeneralDataStore generalDataStore, NurseStore nurseStore) {
    super(License.class, daoProvider);
    this.generalDataStore = generalDataStore;
    this.nurseStore = nurseStore;
  }

  @Override
  protected Iterable<License> preprocess(Iterable<License> licenses, boolean isCreate) {
    ImmutableMap<Long, String> licenseNamesById = generalDataStore
        .get(stream(licenses).map(License::getLicTypeId).collect(toImmutableSet()))
        .getItems().stream()
        .collect(toImmutableMap(GeneralData::getId, GeneralData::getName));
    ImmutableMap<Long, String> nurseNamesById = nurseStore
        .get(stream(licenses).map(License::getNurseId).collect(toImmutableSet()))
        .getItems().stream()
        .collect(toImmutableMap(Nurse::getId, Nurse::fullName));

    licenses.forEach(lic -> lic
        .setLicTypeName(licenseNamesById.get(lic.getLicTypeId()))
        .setNurseName(nurseNamesById.get(lic.getNurseId())));

    return super.preprocess(licenses, isCreate);
  }
}
