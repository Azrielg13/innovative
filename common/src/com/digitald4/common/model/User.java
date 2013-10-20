package com.digitald4.common.model;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.digitald4.common.dao.UserDAO;
import com.digitald4.common.util.Calculate;

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
	
	public static String encodePassword(String password) throws NoSuchAlgorithmException {
		return Calculate.md5(password);
	}
	
	public static User get(String login, String passwd) throws Exception {
		List<User> coll = User.getCollection(new String[]{"" + (login.contains("@") ? PROPERTY.EMAIL : PROPERTY.USER_NAME), "" + PROPERTY.PASSWORD}, login, encodePassword(passwd));
		if (coll.size() > 0) {
			return coll.get(0);
		}
		return null;
	}
	
	public static User getByEmail(String email) {
		List<User> coll = User.getCollection(new String[]{"" + PROPERTY.EMAIL}, email);
		if (coll.size() > 0) {
			return coll.get(0);
		}
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
		return getType() == GenData.UserType_Admin.get();
	}
	
	public boolean isOfRank(GeneralData level) {
		return getType().getRank() <= level.getRank();
	}
	
	public User setLastLogin() throws Exception {
		return setLastLogin(DateTime.now());
	}
	
	public User setPasswordRaw(String password) throws Exception {
		return setPassword(encodePassword(password));
	}
	
	@Override
	public User setEmail(String email) throws Exception {
		if (email != null) {
			setUserName(email.substring(0, email.indexOf('@')));
		}
		return super.setEmail(email);
	}
	
	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
}
