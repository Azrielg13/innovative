package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSetMultimap.toImmutableSetMultimap;

import com.digitald4.common.model.ModelObject;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.UserStore;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.Vendor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Streams;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Set;


public class EntityStore {
  private final Provider<DAO> daoProvider;

  @Inject
  public EntityStore(Provider<DAO> daoProvider) {
    this.daoProvider = daoProvider;
  }

  /** Takes a Set of Pairs of EntityType and EntityId **/
  public ImmutableList<ModelObject<?>> getEntities(Set<Pair<String, Long>> entityTypeIds) {
    return getEntities(entityTypeIds.stream().collect(toImmutableSetMultimap(Pair::getLeft, Pair::getRight)));
  }

  public ImmutableList<ModelObject<?>> getEntities(ImmutableSetMultimap<String, Long> entityIds) {
    DAO dao = daoProvider.get();
    return Streams
        .concat(
            dao.get(Nurse.class, entityIds.get("Nurse")).getItems().stream(),
            dao.get(Patient.class, entityIds.get("Patient")).getItems().stream(),
            dao.get(User.class, entityIds.get("User")).getItems().stream(),
            dao.get(Vendor.class, entityIds.get("Vendor")).getItems().stream())
        .collect(toImmutableList());
  }

  public static String getEntityTypeId(ModelObject<?> obj) {
    return obj.getClass().getSimpleName() + "-" + obj.getId();
  }
}
