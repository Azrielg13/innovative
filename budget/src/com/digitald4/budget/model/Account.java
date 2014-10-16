package com.digitald4.budget.model;
import com.digitald4.budget.dao.AccountDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="budget",name="account")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Account o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Account o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Account o"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM account o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Account extends AccountDAO{
	public Account(){
	}
	public Account(Integer id){
		super(id);
	}
	public Account(Account orig){
		super(orig);
	}
}
