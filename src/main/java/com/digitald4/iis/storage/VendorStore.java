package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Transaction.Op;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

public class VendorStore extends GenericLongStore<Vendor> {
  private final Provider<DAO> daoProvider;

  @Inject
  public VendorStore(Provider<DAO> daoProvider) {
    super(Vendor.class, daoProvider);
    this.daoProvider = daoProvider;
  }

  @Override
  protected Op<Vendor> postprocess(Op<Vendor> op) {
    DAO dao = daoProvider.get();
    Vendor vendor = op.getEntity();
    String name = vendor.getName();
    if (op.getCurrent() != null && !Objects.equals(op.getEntity().getName(), op.getCurrent().getName())) {
      // If the name has changed, we need to update the patients and appointments with the new name.
      dao.update(Patient.class,
          dao.list(Patient.class, Query.forList(Filter.of("billingVendorId", vendor.getId())))
              .getItems().stream().map(Patient::getId).collect(toImmutableList()),
          patient -> patient.setBillingVendorName(name));
      dao.update(Appointment.class,
          dao.list(Appointment.class, Query.forList(Filter.of("vendorId", vendor.getId())))
              .getItems().stream().map(Appointment::getId).collect(toImmutableList()),
          appointment -> appointment.setVendorName(name));
    }
    return op;
  }
}
