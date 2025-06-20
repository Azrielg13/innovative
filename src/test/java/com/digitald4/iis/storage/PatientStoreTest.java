package com.digitald4.iis.storage;

import static com.google.common.truth.Truth.assertThat;
import static java.time.Instant.ofEpochMilli;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.storage.ChangeTracker;
import com.digitald4.common.storage.SearchIndexer;
import com.digitald4.common.storage.testing.DAOTestingImpl;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Patient.ReferralResolution;
import com.digitald4.iis.model.Patient.Status;
import com.digitald4.iis.model.User;
import java.time.Clock;
import org.junit.Before;
import org.junit.Test;

public class PatientStoreTest {
  private User user = new User().setUsername("eddiemay");
  private final SearchIndexer searchIndexer = mock(SearchIndexer.class);
  private final Clock clock = mock(Clock.class);
  private PatientStore patientStore;

  @Before
  public void setup() {
    var changeTracker = new ChangeTracker(() -> user, null, searchIndexer, clock);
    DAOTestingImpl dao = new DAOTestingImpl(changeTracker);
    patientStore = new PatientStore(() -> dao, clock);
  }

  @Test
  public void referralDateFieldsProgress() {
    when(clock.instant())
        .thenReturn(ofEpochMilli(999)).thenReturn(ofEpochMilli(999)).thenReturn(ofEpochMilli(2000));
    Patient patient = new Patient().setId(45L);

    assertThat(patient.getStatus()).isEqualTo(Status.Pending);
    assertThat(patient.getReferralDate()).isNull();

    patientStore.create(patient);
    assertThat(patient.getStatus()).isEqualTo(Status.Pending);
    assertThat(patient.getCreationTime().toEpochMilli()).isEqualTo(999L);
    assertThat(patient.getReferralDate().toEpochMilli()).isEqualTo(999L);

    Patient updated = patientStore.update(patient.getId(), p -> p.setStatus(Status.Denied));

    assertThat(updated.getStatus()).isEqualTo(Status.Denied);
    assertThat(updated.getCreationTime().toEpochMilli()).isEqualTo(999L);
    assertThat(updated.getReferralDate().toEpochMilli()).isEqualTo(999L);
    assertThat(updated.getReferralResolutionDate().toEpochMilli()).isEqualTo(2000L);
    assertThat(updated.getReferralResolution()).isEqualTo(ReferralResolution.Declined);
  }
}
