package com.digitald4.iis.model;
import com.digitald4.iis.dao.PaystubDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="paystub")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Paystub o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Paystub o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Paystub o"),//AUTO-GENERATED
	@NamedQuery(name = "findByNurse", query="SELECT o FROM Paystub o WHERE o.NURSE_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM paystub o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Paystub extends PaystubDAO{
	public Paystub(){
	}
	public Paystub(Integer id){
		super(id);
	}
	public Paystub(Paystub orig){
		super(orig);
	}
}
