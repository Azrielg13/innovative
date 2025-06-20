package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Query;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.User.Role;
import javax.inject.Inject;
import javax.inject.Provider;

public class UserStore extends com.digitald4.common.storage.GenericUserStore<User> {
  private final NurseStore nurseStore;

  @Inject
  public UserStore(Provider<DAO> daoProvider, NurseStore nurseStore) {
    super(User.class, daoProvider);
    this.nurseStore = nurseStore;
  }

  @Override
  public User getBy(String username) {
    User user = super.getBy(username);
    if (user != null) {
      return user;
    }

    Nurse nurse = nurseStore
        .list(Query.forList(null, (username.contains("@") ? "email" : "username") + "=" + username,
            null, 0, 0))
        .getItems()
        .stream()
        .findFirst()
        .orElse(null);

    if (nurse != null) {
      return new User()
          .setId(nurse.getId())
          .setUsername(nurse.getUsername())
          .setFirstName(nurse.getFirstName())
          .setLastName(nurse.getLastName())
          .setRole(Role.Nurse)
          .setHireDate(nurse.getHireDate())
          .setAddress(nurse.getAddress())
          .setPhoneNumber(nurse.getPhoneNumber())
          .setEmail(nurse.getEmail())
          .setStatus(nurse.getStatus());
    }

    return null;
  }
}
