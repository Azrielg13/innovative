package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;

public class LicenseStore extends GenericLongStore<License> {
  private final NurseStore nurseStore;

  @Inject
  public LicenseStore(Provider<DAO> daoProvider, NurseStore nurseStore) {
    super(License.class, daoProvider);
    this.nurseStore = nurseStore;
  }

  @Override
  protected Iterable<License> preprocess(Iterable<License> entities, boolean isCreate) {
    Map<Long, Nurse> nurses = new HashMap<>();
    return super.preprocess(stream(entities)
        .map(
            lic -> lic.setNurseName(
                nurses.computeIfAbsent(lic.getNurseId(), nurseStore::get).fullName()))
        .collect(toImmutableList()), isCreate);
  }
}
