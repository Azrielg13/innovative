package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.service.EntityServiceBulkImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.storage.Store;
import com.digitald4.iis.model.Report;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.User.RoleAbb;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import javax.inject.Inject;

@Api(
    name = "reports",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class ReportService extends EntityServiceBulkImpl<String, Report> {
  @Inject
  ReportService(Store<Report, String> store, LoginResolver loginResolver) {
    super(store, loginResolver);
  }

  @Override @ApiMethod(httpMethod = "GET", path = "list")
  public QueryResult<Report> list(
      @Nullable @Named("fields") String fields, @Nullable @Named("filter") String filter,
      @Nullable @Named("orderBy") String orderBy, @Named("pageSize") @DefaultValue("200") int pageSize,
      @Named("pageToken") @DefaultValue("1") int pageToken, @Nullable @Named("idToken") String idToken)
      throws ServiceException {
    try {
      User user = resolveLogin(idToken, true).user();
      RoleAbb role = user.getRoleAbb();
      var result = getStore().list(Query.forList(fields, filter, orderBy, pageSize, pageToken));
      var reports = result.getItems().stream().filter(report -> report.meetsCriteria(role))
          .collect(toImmutableSet());
      return QueryResult.of(Report.class, reports,
        result.getTotalSize() - (result.getItems().size() - reports.size()), result.query());
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
  }
}
