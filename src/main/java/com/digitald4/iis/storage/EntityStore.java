package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSetMultimap.toImmutableSetMultimap;

import com.digitald4.common.model.ModelObject;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.UserStore;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.Vendor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Streams;

import javax.inject.Inject;
import java.util.Set;


public class EntityStore {
  private final NurseStore nurseStore;
  private final PatientStore patientStore;
  private final Store<Vendor, Long> vendorStore;
  private final UserStore<User> userStore;

  @Inject
  public EntityStore(
      NurseStore nurseStore, PatientStore patientStore, UserStore<User> userStore, Store<Vendor, Long> vendorStore) {
    this.nurseStore = nurseStore;
    this.patientStore = patientStore;
    this.userStore = userStore;
    this.vendorStore = vendorStore;
  }

  public ImmutableList<ModelObject<?>> getEntities(Set<Pair<String, Long>> entityTypeIds) {
    return getEntities(entityTypeIds.stream().collect(toImmutableSetMultimap(Pair::getLeft, Pair::getRight)));
  }

  public ImmutableList<ModelObject<?>> getEntities(ImmutableSetMultimap<String, Long> entities) {
    return Streams
        .concat(
            nurseStore.get(entities.get("Nurse")).getItems().stream(),
            patientStore.get(entities.get("Patient")).getItems().stream(),
            userStore.get(entities.get("User")).getItems().stream(),
            vendorStore.get(entities.get("Vendor")).getItems().stream())
        .collect(toImmutableList());
  }

  public static String getEntityTypeId(ModelObject<?> obj) {
    return obj.getClass().getSimpleName() + "-" + obj.getId();
  }
}
