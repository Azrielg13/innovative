package com.digitald4.common.model;
import com.digitald4.common.dao.GeneralDataDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="GENERAL_DATA")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM GeneralData o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM GeneralData o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM GeneralData o WHERE o.DELETED_TS IS NULL"),//AUTO-GENERATED
	@NamedQuery(name = "findByGroup", query="SELECT o FROM GeneralData o WHERE o.GROUP_ID=?1 AND o.DELETED_TS IS NULL"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM GENERAL_DATA o WHERE o.ID=?"),//AUTO-GENERATED
})
public class GeneralData extends GeneralDataDAO{
	public GeneralData(){
	}
	public GeneralData(Integer id){
		super(id);
	}
	public GeneralData(GeneralData orig){
		super(orig);
	}
	public static GeneralData getInstance(GeneralData group, Integer inGroupId) {
		for(GeneralData gd:getCollection(new String[]{""+PROPERTY.IN_GROUP_ID},group==null?null:group.getId()))
			if(gd.getInGroupId()==inGroupId)
				return gd;
		System.err.println("Missing GeneralData ("+group+","+inGroupId+")");
		return null;
	}
	public String toString(){
		return getName();
	}
}
