package com.digitald4.budget.model;
import com.digitald4.budget.dao.PortfolioUserDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="budget",name="portfolio_user")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM PortfolioUser o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM PortfolioUser o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM PortfolioUser o"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM portfolio_user o WHERE o.ID=?"),//AUTO-GENERATED
})
public class PortfolioUser extends PortfolioUserDAO {
	public PortfolioUser() {
	}
	
	public PortfolioUser(Integer id) {
		super(id);
	}
	
	public PortfolioUser(PortfolioUser orig) {
		super(orig);
	}
}
