package com.digitald4.common.model;
import java.util.Collection;

import com.digitald4.common.dao.UserDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;
@Entity
@Table(schema="common",name="user")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM User o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM User o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM User o"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM user o WHERE o.ID=?"),//AUTO-GENERATED
})
public class User extends UserDAO {
	public static User get(String email, String passwd) {
		Collection<User> coll = User.getCollection(new String[]{""+PROPERTY.EMAIL,""+PROPERTY.PASSWORD}, email, passwd);
		if(coll.size() > 0)
			return coll.iterator().next();
		return null;
	}
	public static User getByEmail(String email) {
		Collection<User> coll = User.getCollection(new String[]{""+PROPERTY.EMAIL}, email);
		if(coll.size() > 0)
			return coll.iterator().next();
		return null;
	}
	public User() {
	}
	public User(Integer id) {
		super(id);
	}
	public User(User orig) {
		super(orig);
	}
	public boolean isAdmin() {
		return getType()==GenData.UserType_Admin.get();
	}
	public boolean isOfRank(GeneralData level) {
		return getType().getRank()<=level.getRank();
	}
	public User setLastLogin() throws Exception {
		setLastLogin(DateTime.now());
		return this;
	}
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
}
