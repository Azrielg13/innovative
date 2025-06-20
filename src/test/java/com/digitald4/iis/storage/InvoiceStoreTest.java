package com.digitald4.iis.storage;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.model.DataFile;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Store;
import com.digitald4.common.util.JSONUtil;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.ServiceCode.Unit;
import com.digitald4.iis.model.Vendor;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class InvoiceStoreTest {
  @Mock private final DAO dao = mock(DAO.class);
  private final Provider<DAO> daoProvider = () -> dao;
  private final ServiceCodeStore serviceCodeStore = mock(ServiceCodeStore.class);
  private final Clock clock = mock(Clock.class);
  @Mock private AppointmentStore appointmentStore = mock(AppointmentStore.class);
  @Mock private InvoiceReportCreator invoiceReportCreator = mock(InvoiceReportCreator.class);
  @Mock private Store<DataFile, String> dataFileStore = mock(Store.class);
  private InvoiceStore invoiceStore;

  @Before
  public void setUp() {
    invoiceStore = new InvoiceStore(daoProvider, appointmentStore, dataFileStore, invoiceReportCreator, null, clock);
    when(dao.create(any(Invoice.class))).thenAnswer(i -> i.getArgument(0));
    when(invoiceReportCreator.createPDF(any(Invoice.class))).thenReturn(new ByteArrayOutputStream());
    when(dataFileStore.create(any(DataFile.class))).thenAnswer(i -> i.getArgument(0));
    when(dao.get(eq(Vendor.class), eq(450L))).thenReturn(new Vendor().setId(450L).setName("Vendor 450"));
  }

  @Test
  public void recreatable() {
    var appointments = ImmutableList.of(
        new Appointment().setId(234L).setDate(1000L).setVendorId(450L).setBillingInfo(
            new AccountingInfo().setUnit(Unit.Hour).setUnitCount(4).setUnitRate(100)),
        new Appointment().setId(235L).setDate(2000L).setVendorId(450L).setBillingInfo(
            new AccountingInfo().setUnit(Unit.Hour).setUnitCount(4).setUnitRate(100)),
        new Appointment().setId(236L).setDate(3000L).setVendorId(450L).setBillingInfo(
            new AccountingInfo().setUnit(Unit.Hour).setUnitCount(4).setUnitRate(100)));
    Invoice invoice = invoiceStore.create(new Invoice().setId(123L).setDate(1000L), appointments);
    assertThat(invoice.getTotalDue()).isEqualTo(1200.00);

    Invoice recreated = invoiceStore.create(JSONUtil.copy(invoice), appointments);
    assertThat(recreated.getTotalDue()).isEqualTo(invoice.getTotalDue());
  }
}
