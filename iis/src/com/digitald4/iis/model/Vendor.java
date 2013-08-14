package com.digitald4.iis.model;
import com.digitald4.iis.dao.VendorDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="iis",name="vendor")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Vendor o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Vendor o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Vendor o"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM vendor o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Vendor extends VendorDAO{
	public Vendor(){
	}
	public Vendor(Integer id){
		super(id);
	}
	public Vendor(Vendor orig){
		super(orig);
	}
	public String toString() {
		return getName();
	}
}
