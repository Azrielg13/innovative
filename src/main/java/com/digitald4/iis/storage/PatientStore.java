package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Patient.ReferralResolution;
import com.digitald4.iis.model.Patient.Status;
import com.digitald4.iis.model.Vendor;
import com.google.common.collect.ImmutableMap;

import java.time.Clock;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

public class PatientStore extends GenericLongStore<Patient> {
  private final Provider<DAO> daoProvider;
  private final Clock clock;

  @Inject
  public PatientStore(Provider<DAO> daoProvider, Clock clock) {
    super(Patient.class, daoProvider);
    this.daoProvider = daoProvider;
    this.clock = clock;
  }

  @Override
  protected Iterable<Patient> preprocess(Iterable<Pair<Patient, Patient>> patients) {
    ImmutableMap<Long, String> vendorNames = daoProvider.get()
        .get(Vendor.class, stream(patients).map(Pair::getLeft).map(Patient::getBillingVendorId).filter(Objects::nonNull).collect(toImmutableSet()))
        .getItems().stream()
        .filter(vendor -> Objects.nonNull(vendor.getName()))
        .collect(toImmutableMap(Vendor::getId, Vendor::getName));

    stream(patients).forEach(pair -> {
      Patient p = pair.getLeft();
      Patient orig = pair.getRight();
      p.setBillingVendorName(vendorNames.get(p.getBillingVendorId()));
      if (orig == null && p.getCreationTime() == null) {
        p.setCreationTime(clock.instant());
      }
      if (orig == null && p.getReferralDate() == null && p.getStatus() == Status.Pending) {
        // If this newly created and Referral date is not set, set it.
        p.setReferralDate(clock.instant());
      } else if (orig != null && orig.getStatus() == Status.Pending && p.getStatus() != Status.Pending) {
        // If we are moving from Pending to another status, set the resolution timestamp.
        p.setReferralResolutionDate(clock.instant())
            .setReferralResolution(switch (p.getStatus()) {
              case Active -> ReferralResolution.Accepted;
              case Denied -> ReferralResolution.Declined;
              case Cancelled -> ReferralResolution.Cancelled;
              default -> ReferralResolution.Accepted;
            });
      }
    });

    return super.preprocess(patients);
  }
}
