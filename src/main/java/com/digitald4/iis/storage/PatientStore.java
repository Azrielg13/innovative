package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.iis.model.Patient;
import javax.inject.Inject;
import javax.inject.Provider;

public class PatientStore extends GenericLongStore<Patient> {

  @Inject
  public PatientStore(Provider<DAO> daoProvider) {
    super(Patient.class, daoProvider);
  }
}
