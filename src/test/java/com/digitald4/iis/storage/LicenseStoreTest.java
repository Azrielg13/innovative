package com.digitald4.iis.storage;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.User.Role;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class LicenseStoreTest {

  @Mock private DAO dao = mock(DAO.class);
  private User admin = new User().setId(1000L).setRole(Role.Administrator);
  private User nurse = new User().setId(1001L).setRole(Role.Nurse);
  private LicenseStore licenseStore;

  @Before
  public void setup() {
    when(dao.list(eq(License.class), any())).then(i -> query(i.getArgument(1, Query.List.class)));
  }

  public QueryResult<License> query(Query.List query) {
    if (query.getFilters().stream().map(Filter::getColumn).anyMatch(col -> col.equals("nurseId"))) {
      return QueryResult.of(License.class, ImmutableList.of(new License().setNurseName("Nurse1")), 1, query);
    }
    return QueryResult.of(License.class, ImmutableList.of(
        new License().setNurseName("Nurse1"), new License().setNurseName("Nurse2")), 1, query);
  }

  @Test
  public void nurseList() {
    licenseStore = new LicenseStore(() -> dao, () -> nurse);

    var licenses = licenseStore.list(Query.forList().setFilters(
        Filter.of("nurseStatus", "Active"),
        Filter.of("expirationDate", ">=", 1750554087014L),
        Filter.of("expirationDate", "<=", 1760922087014L)));

    assertThat(licenses.getItems()).hasSize(1);
  }

  @Test
  public void adminList() {
    licenseStore = new LicenseStore(() -> dao, () -> admin);

    var licenses = licenseStore.list(Query.forList().setFilters(
        Filter.of("nurseStatus", "Active"),
        Filter.of("expirationDate", ">=", 1750554087014L),
        Filter.of("expirationDate", "<=", 1760922087014L)));

    assertThat(licenses.getItems()).hasSize(2);
  }
}
