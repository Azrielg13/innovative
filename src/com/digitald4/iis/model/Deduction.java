package com.digitald4.iis.model;
import com.digitald4.iis.dao.DeductionDAO;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="iis",name="deduction")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Deduction o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Deduction o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Deduction o"),//AUTO-GENERATED
	@NamedQuery(name = "findByPaystub", query="SELECT o FROM Deduction o WHERE o.PAYSTUB_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM deduction o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Deduction extends DeductionDAO{
	public Deduction(EntityManager entityManager) {
		super(entityManager);
	}
	
	public Deduction(EntityManager entityManager, Integer id) {
		super(entityManager, id);
	}
	
	public Deduction(EntityManager entityManager, Deduction orig) {
		super(entityManager, orig);
	}
	
	public boolean isPreTax() {
		return getType().getGroup() == GenData.DEDUCTION_TYPE_PRE_TAX.get(getEntityManager());
	}
	
	public boolean isTax() {
		return getType().getGroup() == GenData.DEDUCTION_TYPE_TAX.get(getEntityManager());
	}
	
	public boolean isPostTax() {
		return getType().getGroup() == GenData.DEDUCTION_TYPE_POST_TAX.get(getEntityManager());
	}

	public void calc(double base, Paystub prev) throws Exception {
		if (getFactor() > 0) {
			setAmount(getFactor() * base);
		}
		double ytd = getAmount();
		if (prev != null) {
			ytd += prev.getDeductionYTD(this);
		}
		setAmountYTD(ytd);
	}
}
