package com.digitald4.common.model;
import com.digitald4.common.dao.DataFileDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="common",name="data_file")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM DataFile o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM DataFile o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM DataFile o"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM data_file o WHERE o.ID=?"),//AUTO-GENERATED
})
public class DataFile extends DataFileDAO{
	public DataFile(){
	}
	public DataFile(Integer id){
		super(id);
	}
	public DataFile(DataFile orig){
		super(orig);
	}
}
