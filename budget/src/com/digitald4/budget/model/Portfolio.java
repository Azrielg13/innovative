package com.digitald4.budget.model;
import com.digitald4.budget.dao.PortfolioDAO;
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
}
