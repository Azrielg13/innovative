package com.digitald4.budget.model;
import java.util.ArrayList;
import java.util.List;

import com.digitald4.budget.dao.PortfolioDAO;
import com.digitald4.common.model.GeneralData;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="budget",name="portfolio")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Portfolio o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Portfolio o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Portfolio o"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM portfolio o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Portfolio extends PortfolioDAO{
	public Portfolio(){
	}
	public Portfolio(Integer id){
		super(id);
	}
	public Portfolio(Portfolio orig){
		super(orig);
	}
	public List<Account> getAccounts(GeneralData type) {
		List<Account> accounts = new ArrayList<Account>();
		for (Account account : getAccounts()) {
			if (account.getCategory() == type) {
				accounts.add(account);
			}
		}
		return accounts;
	}
	public List<Bill> getBills() {
		List<Bill> bills = new ArrayList<Bill>();
		for (Account account : getAccounts()) {
			bills.addAll(account.getBills());
		}
		return bills;
	}
}
