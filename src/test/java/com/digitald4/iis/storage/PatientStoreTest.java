package com.digitald4.iis.storage;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.server.service.BulkGetable.MultiListResult;
import com.digitald4.common.storage.DAO;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Patient.ReferralResolution;
import com.digitald4.iis.model.Patient.Status;
import com.google.common.collect.ImmutableList;
import java.time.Clock;
import java.time.Instant;
import java.util.function.UnaryOperator;
import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class PatientStoreTest {

  @Mock
  private final DAO dao = mock(DAO.class);
  private final Provider<DAO> daoProvider = () -> dao;
  private final Clock clock = mock(Clock.class);
  private PatientStore patientStore;

  @Before
  public void setup() {
    patientStore = new PatientStore(daoProvider, clock);
    when(dao.get(any(), anyIterable())).thenReturn(
        MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
    when(dao.create(any(Patient.class))).thenAnswer(i -> i.getArgument(0));
  }

  @Test
  public void referralDateFieldsProgress() {
    when(clock.instant()).thenReturn(Instant.ofEpochMilli(1000))
        .thenReturn(Instant.ofEpochMilli(1000)).thenReturn(Instant.ofEpochMilli(2000));
    Patient patient = new Patient().setId(45L);

    assertThat(patient.getStatus()).isEqualTo(Status.Pending);
    assertThat(patient.getReferralDate()).isNull();

    patientStore.create(patient);
    assertThat(patient.getStatus()).isEqualTo(Status.Pending);
    assertThat(patient.getCreationTime().toEpochMilli()).isEqualTo(1000L);
    assertThat(patient.getReferralDate().toEpochMilli()).isEqualTo(1000L);

    when(dao.update(any(), eq(45L), any()))
        .thenAnswer(i -> ((UnaryOperator<Patient>) i.getArgument(2)).apply(patient));
    Patient updated = patientStore.update(patient.getId(), p -> p.setStatus(Status.Denied));

    assertThat(updated.getStatus()).isEqualTo(Status.Denied);
    assertThat(updated.getCreationTime().toEpochMilli()).isEqualTo(1000L);
    assertThat(updated.getReferralDate().toEpochMilli()).isEqualTo(1000L);
    assertThat(updated.getReferralResolutionDate().toEpochMilli()).isEqualTo(2000L);
    assertThat(updated.getReferralResolution()).isEqualTo(ReferralResolution.Declined);
  }
}
