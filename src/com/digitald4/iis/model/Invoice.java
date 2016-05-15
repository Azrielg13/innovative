package com.digitald4.iis.model;
import java.util.ArrayList;
import java.util.List;

import com.digitald4.iis.dao.InvoiceDAO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;
@Entity
@Table(schema="iis",name="invoice")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Invoice o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Invoice o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Invoice o"),//AUTO-GENERATED
	@NamedQuery(name = "findByVendor", query="SELECT o FROM Invoice o WHERE o.VENDOR_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM invoice o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Invoice extends InvoiceDAO {
	
	public Invoice(EntityManager entityManager) throws Exception {
		super(entityManager);
		setStatus(GenData.PAYMENT_STATUS_UNPAID.get(entityManager));
		setGenerationTime(DateTime.now());
	}
	
	public Invoice(EntityManager entityManager, Integer id){
		super(entityManager, id);
	}
	
	public Invoice(EntityManager entityManager, Invoice orig){
		super(entityManager, orig);
	}
	
	public boolean isUnpaid() throws Exception {
		return getStatus() == null || getStatus() == GenData.PAYMENT_STATUS_UNPAID.get(getEntityManager());
	}
	
	public boolean isPaid() throws Exception {
		return getStatus() == GenData.PAYMENT_STATUS_PAID.get(getEntityManager());
	}
	
	@Override
	public Invoice setTotalPaid(double totalPaid) throws Exception {
		super.setTotalPaid(totalPaid);
		if (totalPaid >= getTotalDue()) {
			setStatus(GenData.PAYMENT_STATUS_PAID.get(getEntityManager()));
		}
		return this;
	}
	
	@Override
	@Column(name = "TOTAL_DUE", nullable = false)
	public double getTotalDue() {
		double total = super.getTotalDue();
		if (total == 0) {
			for (Appointment appointment : getAppointments()) {
				total += appointment.getBillingTotal();
			}
		}
		return total;
	}
	
	public static List<Invoice> getUnpaidInvoices(EntityManager entityManager) {
		return getCollection(Invoice.class, entityManager,
				new String[]{"" + PROPERTY.STATUS_ID}, GenData.PAYMENT_STATUS_UNPAID.get(entityManager).getId());
	}
	
	public static List<Invoice> getPaidInvoices(EntityManager entityManager) {
		return getCollection(Invoice.class, entityManager,
				new String[]{"" + PROPERTY.STATUS_ID}, GenData.PAYMENT_STATUS_PAID.get(entityManager).getId());
	}
	
	@Override
	public void delete() throws Exception {
		for (Appointment app : new ArrayList<Appointment>(getAppointments())) {
			app.setInvoiceId(null).save();
		}
		super.delete();
	}
}
