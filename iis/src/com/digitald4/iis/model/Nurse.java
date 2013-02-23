package com.digitald4.iis.model;
import com.digitald4.iis.dao.NurseDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="iis",name="nurse")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Nurse o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Nurse o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Nurse o WHERE o.DELETED_TS IS NULL"),//AUTO-GENERATED
	@NamedQuery(name = "findByUser", query="SELECT o FROM Nurse o WHERE o.ID=?1 AND o.DELETED_TS IS NULL"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM nurse o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Nurse extends NurseDAO{
	public Nurse(){
	}
	public Nurse(Integer id){
		super(id);
	}
	public Nurse(Nurse orig){
		super(orig);
	}
}
