package com.digitald4.iis.storage;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.server.service.BulkGetable.MultiListResult;
import com.digitald4.common.storage.*;
import com.digitald4.iis.model.*;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class NoteStoreTest {
  @Mock private final DAO dao = mock(DAO.class);
  private static final User USER_1 = new User().setId(85L).setUsername("username@");
  private static final Nurse NURSE_1 = new Nurse().setId(1L).setFirstName("First").setLastName("Nurse");
  private NoteStore noteStore;

  @Before
  public void setup() {
    noteStore = new NoteStore(() -> dao, new EntityStore(() -> dao));
    when(dao.get(eq(Nurse.class), anyIterable())).thenReturn(
        MultiListResult.of(ImmutableList.of(NURSE_1), ImmutableList.of(1L)));
    when(dao.get(eq(Patient.class), anyIterable())).thenReturn(
        MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
    when(dao.get(eq(Vendor.class), anyIterable())).thenReturn(
        MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
    when(dao.get(eq(User.class), anyIterable())).thenReturn(MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
    when(dao.create(anyIterable())).thenAnswer(i -> i.getArgument(0));
    when(dao.create(any(Note.class))).thenAnswer(i -> i.getArgument(0));
  }

  @Test
  public void create() {
    assertThat(noteStore.create(new Note().setNote("Test Note").setEntityType("Nurse").setEntityId("1"))).isEqualTo(
        new Note().setNote("Test Note").setEntityType("Nurse").setEntityId("1").setEntityName("First Nurse"));
            // .setCreationUsername("username@"));
  }
}
