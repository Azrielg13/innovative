package com.digitald4.iis.storage;

import static com.digitald4.common.storage.Query.forList;
import static com.digitald4.iis.model.ServiceCode.Type.Pay;
import static com.digitald4.iis.model.ServiceCode.Unit.Hour;
import static com.digitald4.iis.model.ServiceCode.Unit.Visit;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableListMultimap.toImmutableListMultimap;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.Streams.concat;
import static com.google.common.collect.Streams.stream;
import static java.util.function.Function.identity;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.List;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.ServiceCode;
import com.digitald4.iis.model.ServiceCode.Unit;
import com.digitald4.iis.model.Vendor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Provider;

public class ServiceCodeStore extends GenericStore<ServiceCode, String> {
  private static final String UNIT_KEY = "%s-%f";
  private final Provider<DAO> daoProvider;

  @Inject
  public ServiceCodeStore(Provider<DAO> daoProvider) {
    super(ServiceCode.class, daoProvider);
    this.daoProvider = daoProvider;
  }

  @Override
  public QueryResult<ServiceCode> list(List query) {
    if (!query.getFilters().isEmpty() && query.getFilters().get(0).getColumn().equals("nurseId")) {
      Query.Filter firstFilter = query.getFilters().get(0);
      Object value = firstFilter.getValue();
      return listForNurses(
          firstFilter.getOperator().equals("IN")
              ? ((Collection<?>) value).stream().map(v -> Long.parseLong(v.toString())).collect(toImmutableList())
              : ImmutableList.of(Long.parseLong(firstFilter.getValue().toString())));
    }

    return super.list(query);
  }

  @Override
  protected Iterable<ServiceCode> preprocess(Iterable<ServiceCode> serviceCodes, boolean isCreate) {
    super.preprocess(serviceCodes, isCreate);

    ImmutableMap<Long, String> vendorNames = daoProvider.get()
        .get(Vendor.class,
            stream(serviceCodes).map(ServiceCode::getVendorId).filter(Objects::nonNull).collect(toImmutableList()))
        .getItems()
        .stream().collect(toImmutableMap(Vendor::getId, Vendor::toString));

    return stream(serviceCodes)
        .map(serviceCode -> serviceCode.setVendorName(vendorNames.get(serviceCode.getVendorId())))
        .collect(toImmutableList());
  }

  public ServiceCode getForVendor(long vendorId, String billCode) {
    return get(String.format("%s-%s", vendorId, billCode));
  }

  public ImmutableList<ServiceCode> getForVendor(long vendorId) {
    return list(Query.forList(Filter.of("VendorId", vendorId), Filter.of("active", true))).getItems();
  }

  public QueryResult<ServiceCode> listForNurses(Iterable<Long> nurseIds) {
    ImmutableListMultimap<String, ServiceCode> serviceCodes =
        list(forList(Filter.of("type", Pay), Filter.of("active", true))).getItems().stream()
        .collect(toImmutableListMultimap(sc -> String.format(UNIT_KEY, sc.getUnit(), sc.getUnitPrice()), identity()));

    ImmutableList<ServiceCode> results = daoProvider.get().get(Nurse.class, nurseIds).getItems().stream()
        .flatMap(nurse -> Stream
            .of(Pair.of(Hour, nurse.getPayRate()), Pair.of(Visit, nurse.getPayFlat()))
            .filter(pair -> pair.getRight() != null)
            .flatMap(pair -> serviceCodes.get(String.format(UNIT_KEY, pair.getLeft(), pair.getRight())).stream()
                .map(serviceCode -> serviceCode.copy().setNurseInfo(nurse.getId(), nurse.fullName()))))
        .collect(toImmutableList());

    return QueryResult.of(ServiceCode.class, results, results.size(), null);
  }

  public ImmutableList<ServiceCode> getForNurse(Nurse nurse, Unit unit) {
    Double unitPrice = unit == Hour ? nurse.getPayRate() : nurse.getPayFlat();
    return list(forList(Filter.of("type", Pay), Filter.of("active", true), Filter.of("unit", unit),
        Filter.of("unitPrice", unitPrice))).getItems();
  }

  public ImmutableList<ServiceCode> getForNurse(Nurse nurse) {
    return concat(getForNurse(nurse, Hour).stream(), getForNurse(nurse, Visit).stream()).collect(toImmutableList());
  }
}
