package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.Vendor;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Provider;

@Api(
    name = "search",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class SearchService {
  private final Provider<DAO> daoProvider;
  private final LoginResolver loginResolver;

  @Inject
  public SearchService(Provider<DAO> daoProvider, LoginResolver loginResolver) {
    this.daoProvider = daoProvider;
    this.loginResolver = loginResolver;
  }

  @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "search")
  public ImmutableList<QueryResult> search(@Named("searchText") String searchText, @Named("idToken") String idToken,
      @Nullable @Named("orderBy") String orderBy, @Named("pageSize") @DefaultValue("50") int pageSize,
      @Named("pageToken") @DefaultValue("1") int pageToken) throws ServiceException {
    try {
      loginResolver.resolve(idToken, true);
      DAO dao = daoProvider.get();
      var searchQuery = Query.forSearch(searchText, orderBy, pageSize, pageToken);
      return Stream.of(Nurse.class, Patient.class, User.class, Vendor.class)
          .map(cls -> dao.search(cls, searchQuery))
          .collect(toImmutableList());
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
  }
}
