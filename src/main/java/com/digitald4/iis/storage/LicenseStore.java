package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.List;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.storage.Transaction.Op;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.User.Role;
import com.google.common.collect.ImmutableMap;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.function.Function;

public class LicenseStore extends GenericStore<License, String> {
  private final Provider<DAO> daoProvider;
  private final Provider<User> userProvider;

  @Inject
  public LicenseStore(Provider<DAO> daoProvider, Provider<User> userProvider) {
    super(License.class, daoProvider);
    this.daoProvider = daoProvider;
    this.userProvider = userProvider;
  }

  @Override
  public QueryResult<License> list(List query) {
    User user = userProvider.get();
    if (user.getRole() == Role.Nurse && query.getFilters().stream().map(Filter::getColumn)
        .noneMatch(column -> column.equals("nurseId"))) {
      query.addFilter(Filter.of("nurseId", user.getId()));
    }
    return super.list(query);
  }

  @Override
  protected Iterable<Op<License>> preprocess(Iterable<Op<License>> ops) {
    DAO dao = daoProvider.get();
    User user = userProvider.get();
    var licenses = stream(ops).map(Op::getEntity).collect(toImmutableList());
    ImmutableMap<Long, String> licenseNamesById = dao
        .get(GeneralData.class, licenses.stream().map(License::getLicTypeId).collect(toImmutableSet()))
        .getItems().stream()
        .collect(toImmutableMap(GeneralData::getId, GeneralData::getName));
    ImmutableMap<Long, Nurse> nursesById = dao
        .get(Nurse.class, licenses.stream().map(License::getNurseId).collect(toImmutableSet()))
        .getItems().stream()
        .collect(toImmutableMap(Nurse::getId, Function.identity()));

    licenses.forEach(lic -> {
      lic.setLicTypeName(licenseNamesById.get(lic.getLicTypeId()))
          .setNurseName(nursesById.get(lic.getNurseId()).fullName())
          .setNurseStatus(nursesById.get(lic.getNurseId()).getStatus());
      if (user.getRole() == Role.Nurse) {
        lic.setNeedsReview(true);
      }
    });

    return ops;
  }
}
