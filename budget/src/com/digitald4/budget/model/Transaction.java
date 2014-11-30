package com.digitald4.budget.model;

import java.util.Date;
import java.util.List;

import com.digitald4.budget.dao.TransactionDAO;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.json.JSONException;
import org.json.JSONObject;
@Entity
@Table(schema="budget",name="transaction")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Transaction o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Transaction o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Transaction o"),//AUTO-GENERATED
	@NamedQuery(name = "findByPortfolio", query="SELECT o FROM Transaction o WHERE o.PORTFOLIO_ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findByBill", query="SELECT o FROM Transaction o WHERE o.BILL_ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findByDebitAccount", query="SELECT o FROM Transaction o WHERE o.DEBIT_ACCOUNT_ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findByCreditAccount", query="SELECT o FROM Transaction o WHERE o.CREDIT_ACCOUNT_ID=?1"),//AUTO-GENERATED
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
		if (date == null && getBill() != null) {
			date = getBill().getPaymentDate();
		}
		return date;
	}
	
	public List<Transaction> getDebitAcctPreTrans() {
		return getPortfolio().getTransactions();
	}
	
	private double getAcctBalPre(Account account) {
		double bal = 0;
		Integer acctId = account.getId();
		for (Transaction trans : getDebitAcctPreTrans()) {
			if (compareTo(trans) != 1) {
				continue;
			}
			if (acctId.equals(trans.getDebitAccountId())) {
				bal -= trans.getAmount();
			}
			if (acctId.equals(trans.getCreditAccountId())) {
				bal += trans.getAmount();
			}
		}
		return bal;
	}
	
	public double getDebitAcctBalPre() {
		return getAcctBalPre(getDebitAccount());
	}
	
	public double getDebitAcctBalPost() {
		return getDebitAcctBalPre() - getAmount();
	}
	
	public List<Transaction> getCreditAcctPreTrans() {
		return getPortfolio().getTransactions();
	}
	
	public double getCreditAcctBalPre() {
		return getAcctBalPre(getCreditAccount());
	}
	
	public double getCreditAcctBalPost() {
		return getCreditAcctBalPre() + getAmount();
	}
	
	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = super.toJSON();
		double amount = getAmount();
		double debitAcctBalPre = getDebitAcctBalPre();
		json.put("debitAcctBalPre", debitAcctBalPre);
		json.put("debitAcctBalPost", (debitAcctBalPre - amount));
		double creditAcctBalPre = getCreditAcctBalPre();
		json.put("creditAcctBalPre", creditAcctBalPre);
		json.put("creditAcctBalPost", (creditAcctBalPre + amount));
		return json;
	}
	
	@Override
	public int compareTo(Object o) {
		Transaction trans = (Transaction)o;
		int ret = getDate().compareTo(trans.getDate());
		if (ret != 0) {
			return ret;
		}
		if (getAmount() > trans.getAmount()) {
			return -1;
		} else if (getAmount() < trans.getAmount()) {
			return 1;
		}
		return super.compareTo(o);
	}
}
