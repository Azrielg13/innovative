package com.digitald4.iis.server;

import static com.digitald4.common.storage.Query.List.forList;
import static com.digitald4.iis.model.ServiceCode.Unit.Hour;
import static com.digitald4.iis.model.ServiceCode.Unit.Visit;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableListMultimap.toImmutableListMultimap;
import static java.util.function.Function.identity;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.service.EntityServiceImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.ServiceCode;
import com.digitald4.iis.storage.NurseStore;
import com.digitald4.iis.storage.ServiceCodeStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Stream;

@Api(
    name = "serviceCodes",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class ServiceCodeService extends EntityServiceImpl<ServiceCode, String> {
  private static final String UNIT_KEY = "%s-%f";
  private final NurseStore nurseStore;

  @Inject
  ServiceCodeService(ServiceCodeStore serviceCodeStore, LoginResolver loginResolver, NurseStore nurseStore) {
    super(serviceCodeStore, loginResolver);
    this.nurseStore = nurseStore;
  }

  @Override
  @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "list")
  public QueryResult<ServiceCode> list(
      @Nullable @Named("filter") String filter, @Nullable @Named("orderBy") String orderBy,
      @Named("pageSize") @DefaultValue("200") int pageSize,
      @Named("pageToken") @DefaultValue("1") int pageToken,
      @Nullable @Named("idToken") String idToken) throws ServiceException {
    try {
      resolveLogin(idToken, "list");
      Query.List query = Query.forList(filter, orderBy, pageSize, pageToken);
      Query.Filter firstFilter = query.getFilters().get(0);
      if (firstFilter.getColumn().equals("nurseId")) {
        Object value = firstFilter.getValue();
        return listForNurses(
            firstFilter.getOperator().equals("IN")
                ? ((Collection<?>) value).stream().map(v -> Long.parseLong(v.toString())).collect(toImmutableList())
                : ImmutableList.of(Long.parseLong(firstFilter.getValue().toString())));
      }
      return transform(getStore().list(query));
    } catch (DD4StorageException e) {
      e.printStackTrace();
      throw new ServiceException(e.getErrorCode(), e);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(DD4StorageException.ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), e);
    }
  }

  @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST, path = "listForNurses")
  public QueryResult<ServiceCode> listForNurses(
      ServiceCodeRequest serviceCodeRequest, @Named("idToken") String idToken) throws ServiceException {
    try {
      resolveLogin(idToken, true);
      return listForNurses(serviceCodeRequest.getNurseIds());
    } catch (DD4StorageException e) {
      e.printStackTrace();
      throw new ServiceException(e.getErrorCode(), e);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(DD4StorageException.ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), e);
    }
  }

  private QueryResult<ServiceCode> listForNurses(Iterable<Long> nurseIds) {
    ImmutableListMultimap<String, ServiceCode> serviceCodes = getStore().list(forList()).getItems().stream()
        .collect(toImmutableListMultimap(sc -> String.format(UNIT_KEY, sc.getUnit(), sc.getUnitPrice()), identity()));

    ImmutableList<ServiceCode> results = nurseStore.get(nurseIds).getItems().stream()
        .flatMap(nurse -> Stream
            .of(Pair.of(Hour, nurse.getPayRate()), Pair.of(Visit, nurse.getPayFlat()),
                Pair.of(Hour, nurse.getPayRate2HrSoc()), Pair.of(Visit, nurse.getPayFlat2HrSoc()),
                Pair.of(Hour, nurse.getPayRate2HrRoc()), Pair.of(Visit, nurse.getPayFlat2HrRoc()))
            .filter(pair -> pair.getRight() != null)
            .flatMap(pair -> serviceCodes.get(String.format(UNIT_KEY, pair.getLeft(), pair.getRight())).stream()
                .map(serviceCode -> serviceCode.copy().setNurseInfo(nurse.getId(), nurse.fullName()))))
        .collect(toImmutableList());

    return QueryResult.of(results, results.size(), null);
  }

  public static class ServiceCodeRequest {
    private ImmutableSet<Long> nurseIds = ImmutableSet.of();

    public ImmutableSet<Long> getNurseIds() {
      return nurseIds;
    }

    public ServiceCodeRequest setNurseIds(Iterable<Long> nurseIds) {
      this.nurseIds = ImmutableSet.copyOf(nurseIds);
      return this;
    }
  }
}
