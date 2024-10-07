package com.digitald4.iis.storage;

import static com.digitald4.iis.model.User.RoleAbb.ADMIN;
import static com.digitald4.iis.model.User.RoleAbb.RC;
import static com.digitald4.iis.model.User.RoleAbb.CC;
import static com.digitald4.iis.model.User.RoleAbb.SB;
import static com.digitald4.iis.model.User.RoleAbb.RCO;
import static com.digitald4.iis.model.User.RoleAbb.CCO;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.concat;

import com.digitald4.common.model.Flag;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.storage.Query.List;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.User.RoleAbb;
import com.google.common.collect.ImmutableList;
import javax.inject.Inject;
import javax.inject.Provider;

public class FlagStore extends GenericStore<Flag, String> {

  private final Provider<User> userProvider;

  @Inject
  public FlagStore(Provider<DAO> daoProvider, Provider<User> userProvider) {
    super(Flag.class, daoProvider);
    this.userProvider = userProvider;
  }

  @Override
  public QueryResult<Flag> list(List query) {
    var flags = concat(super.list(query).getItems().stream(), getPermissions().stream()).collect(toImmutableList());
    return QueryResult.of(Flag.class, flags, flags.size(), null);
  }

  public ImmutableList<Flag> getPermissions() {
    User user = userProvider.get();
    if (user == null) {
      return ImmutableList.of();
    }

    RoleAbb role = user.getRoleAbb();
    if (role == null) {
      role = ADMIN;
      // return ImmutableList.of();
    }

    return ImmutableList.of(
        Flag.of("dashboardEnabled", role == ADMIN || role == CC || role == RCO),
        Flag.of("calendarEnabled", true),
        Flag.of("appointmentsEnabled",
            role == ADMIN || role == CC || role == RCO || role == CCO || role == SB || role == RC),
        Flag.of("pendingAssessmentsEnabled", role == ADMIN || role == RCO || role == CC || role == SB || role == RC),
        Flag.of("billableEnabled", role == ADMIN || role == RCO || role == SB),
        Flag.of("quickBookExportsEnabled", role == ADMIN || role == SB),
        Flag.of("patientsEnabled", role == ADMIN || role == CC || role == RCO || role == RC || role == SB || role == CCO),
        Flag.of("newIntakeEnabled", role == ADMIN || role == RCO),
        Flag.of("pendingIntakeEnabled", role == ADMIN || role == RCO || role == CCO),
        Flag.of("patientNotesEnabled", role == ADMIN || role == RCO || role == CCO),
        Flag.of("nursesEnabled", role == ADMIN || role == CC || role == RCO || role == RC || role == SB || role == CCO),
        Flag.of("nurseAddEnabled", role == ADMIN || role == CC),
        Flag.of("licenseAlertEnabled", role == ADMIN || role == CC || role == RCO || role == CCO),
        Flag.of("payCodesEnabled", role == ADMIN || role == SB),
        Flag.of("nurseNotesEnabled", role == ADMIN || role == CC || role == RCO || role == CCO),
        Flag.of("vendorsEnabled", role == ADMIN || role == RCO || role == SB || role == CCO),
        Flag.of("vendorAddEnabled", role == ADMIN || role == RCO || role == SB),
        Flag.of("billCodesEnabled", role == ADMIN || role == RCO || role == SB),
        Flag.of("vendorNotesEnabled", role == ADMIN || role == RCO || role == SB || role == CCO),
        Flag.of("usersEnabled", role == ADMIN || role == CC || role == RCO || role == RC || role == SB || role == CCO),
        Flag.of("userAddEnabled", role == ADMIN),
        Flag.of("userNotesEnabled", role == ADMIN),
        Flag.of("reportsEnabled", role == ADMIN || role == CC || role == RCO || role == RC || role == SB || role == CCO),
        Flag.of("exportsEnabled", role == ADMIN || role == SB)
    );
  }
}
