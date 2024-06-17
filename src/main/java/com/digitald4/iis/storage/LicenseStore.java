package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import com.google.common.collect.ImmutableMap;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.function.Function;

public class LicenseStore extends GenericStore<License, String> {
  private final Provider<DAO> daoProvider;

  @Inject
  public LicenseStore(Provider<DAO> daoProvider) {
    super(License.class, daoProvider);
    this.daoProvider = daoProvider;
  }

  @Override
  protected Iterable<License> preprocess(Iterable<License> licenses, boolean isCreate) {
    DAO dao = daoProvider.get();
    ImmutableMap<Long, String> licenseNamesById = dao
        .get(GeneralData.class, stream(licenses).map(License::getLicTypeId).collect(toImmutableSet()))
        .getItems().stream()
        .collect(toImmutableMap(GeneralData::getId, GeneralData::getName));
    ImmutableMap<Long, Nurse> nursesById = dao
        .get(Nurse.class, stream(licenses).map(License::getNurseId).collect(toImmutableSet()))
        .getItems().stream()
        .collect(toImmutableMap(Nurse::getId, Function.identity()));

    licenses.forEach(lic -> lic
        .setLicTypeName(licenseNamesById.get(lic.getLicTypeId()))
        .setNurseName(nursesById.get(lic.getNurseId()).fullName())
        .setNurseStatus(nursesById.get(lic.getNurseId()).getStatus()));

    return super.preprocess(licenses, isCreate);
  }
}
