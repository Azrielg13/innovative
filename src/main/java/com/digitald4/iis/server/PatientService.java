package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.ReferralResponse;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.User.Role;
import com.digitald4.iis.storage.PatientStore;
import com.digitald4.iis.storage.ReferralResponseStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.BadRequestException;
import com.google.common.collect.ImmutableList;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Provider;

@Api(
    name = "patients",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class PatientService extends AdminService<Patient> {
  private final ReferralResponseStore referralResponseStore;
  private final Provider<User> userProvider;
  @Inject
  PatientService(PatientStore patientStore, LoginResolver loginResolver, Provider<User> userProvider, ReferralResponseStore referralResponseStore) {
    super(patientStore, loginResolver);
    this.referralResponseStore = referralResponseStore;
    this.userProvider = userProvider;
  }

  @Override
  @ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT, path = "{id}")
  public Patient update(@Named("id") Long id, Patient entity, @Named("updateMask") String updateMask,
      @Nullable @Named("idToken") String idToken) throws ServiceException {
    if (updateMask.equals("response") || updateMask.equals("responseComment")) {
      User user = resolveLogin(idToken, true).user();
      if (user.getRole() != Role.Nurse) {
        throw new BadRequestException("Only nurses are allowed to respond at this time.");
      }
      var referralResponse = new ReferralResponse().setPatientId(id).setNurseId(user.getId());
      var multiListResult = referralResponseStore.get(ImmutableList.of(referralResponse.getId()));
      if (!multiListResult.getItems().isEmpty()) {
        referralResponseStore.update(referralResponse.getId(), current -> {
          if (updateMask.equals("response")) {
            current.setResponse(entity.response());
          } else {
            current.setComment(entity.responseComment());
          }
          return current;
        });
      } else {
        if (updateMask.equals("response")) {
          referralResponse.setResponse(entity.response());
        } else {
          referralResponse.setComment(entity.responseComment());
        }
        referralResponseStore.create(referralResponse);
      }
      return entity;
    }

    return super.update(id, entity, updateMask, idToken);
  }

  @Override
  protected Iterable<Patient> transform(Iterable<Patient> entities) {
    User user = userProvider.get();
    if (user.getRole() == Role.Nurse) {
      var patientMap = stream(entities).collect(toImmutableMap(Patient::getId, Function.identity()));
      referralResponseStore.list(Query.forList(Filter.of(
          "patientId", "IN", stream(entities).map(Patient::getId).collect(toImmutableList()))))
          .getItems().forEach(response -> patientMap.get(response.getPatientId()).setReferralResponse(response));
    }
    return super.transform(entities);
  }
}