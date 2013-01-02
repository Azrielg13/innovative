package com.digitald4.iis.model;
import com.digitald4.iis.dao.LicenseDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="iis",name="LICENSE")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM License o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM License o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM License o WHERE o.DELETED_TS IS NULL"),//AUTO-GENERATED
	@NamedQuery(name = "findByNurse", query="SELECT o FROM License o WHERE o.NURSE_ID=?1 AND o.DELETED_TS IS NULL"),//AUTO-GENERATED
	@NamedQuery(name = "findByLicType", query="SELECT o FROM License o WHERE o.LIC_TYPE_ID=?1 AND o.DELETED_TS IS NULL"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM LICENSE o WHERE o.ID=?"),//AUTO-GENERATED
})
public class License extends LicenseDAO{
	public License(){
	}
	public License(Integer id){
		super(id);
	}
	public License(License orig){
		super(orig);
	}
}
