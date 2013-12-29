package com.digitald4.iis.model;
import com.digitald4.iis.dao.InvoiceDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="iis",name="invoice")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Invoice o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Invoice o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Invoice o"),//AUTO-GENERATED
	@NamedQuery(name = "findByVendor", query="SELECT o FROM Invoice o WHERE o.VENDOR_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM invoice o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Invoice extends InvoiceDAO{
	public Invoice(){
	}
	public Invoice(Integer id){
		super(id);
	}
	public Invoice(Invoice orig){
		super(orig);
	}
}
