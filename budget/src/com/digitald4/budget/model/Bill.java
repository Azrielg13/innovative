package com.digitald4.budget.model;
import java.util.Date;

import com.digitald4.budget.dao.BillDAO;
import com.digitald4.common.component.CalEvent;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;
@Entity
@Table(schema="budget",name="bill")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Bill o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Bill o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Bill o"),//AUTO-GENERATED
	@NamedQuery(name = "findByAccount", query="SELECT o FROM Bill o WHERE o.ACCOUNT_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM bill o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Bill extends BillDAO implements CalEvent {
	public Bill() {
	}
	
	public Bill(Integer id) {
		super(id);
	}
	
	public Bill(Bill orig) {
		super(orig);
	}
	
	public double getPaid() {
		double paid = 0;
		for (Transaction trans : getTransactions()) {
			paid += trans.getAmount();
		}
		return paid;
	}
	
	public double getRemainingDue() {
		return getAmount() - getPaid();
	}
	
	public Date getPaymentDate() {
		Date date = getPaymentDateD();
		if (date == null) {
			date = getDueDate();
		}
		return date;
	}

	@Override
	public DateTime getStart() {
		return new DateTime(getPaymentDate());
	}

	@Override
	public DateTime getEnd() {
		return getStart();
	}

	@Override
	public String getTitle() {
		return getAccount().getName();
	}

	@Override
	public int getDuration() {
		return 0;
	}

	@Override
	public boolean isActiveOnDay(Date date) {
		return getPaymentDate().equals(date);
	}

	@Override
	public boolean isActiveBetween(DateTime start, DateTime end) {
		start = start.minusMillis(1);
		DateTime st = getStart();
		DateTime et = getEnd();
		// Did this event start any time between these periods or did these period start any time during this event
		return (start.isBefore(st) && end.isAfter(st) || st.isBefore(start) && et.isAfter(start));
	}

	@Override
	public boolean isCancelled() {
		return !isActive();
	}
}
