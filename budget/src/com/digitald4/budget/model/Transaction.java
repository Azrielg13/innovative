package com.digitald4.budget.model;

import java.util.Date;

import com.digitald4.budget.dao.TransactionDAO;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="budget",name="transaction")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Transaction o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Transaction o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Transaction o"),//AUTO-GENERATED
	@NamedQuery(name = "findByBill", query="SELECT o FROM Transaction o WHERE o.BILL_ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findByDebitAccount", query="SELECT o FROM Transaction o WHERE o.DEBIT_ACCOUNT_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM transaction o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Transaction extends TransactionDAO {
	public Transaction() {
	}
	
	public Transaction(Integer id) {
		super(id);
	}
	
	public Transaction(Transaction orig) {
		super(orig);
	}
	
	public Date getPaymentDate() {
		Date date = getDate();
		if (date == null) {
			date = getBill().getPaymentDate();
		}
		return date;
	}
}
