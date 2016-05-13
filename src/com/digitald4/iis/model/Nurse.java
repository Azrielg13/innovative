package com.digitald4.iis.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.digitald4.common.component.Notification;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.dao.NurseDAO;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
@Entity
@Table(schema="iis",name="nurse")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Nurse o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Nurse o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Nurse o"),//AUTO-GENERATED
	@NamedQuery(name = "findByUser", query="SELECT o FROM Nurse o WHERE o.ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM nurse o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Nurse extends NurseDAO {
	
	public Nurse() {
		try {
			setStatus(GenData.NURSE_STATUS_PENDING.get());
			setRegDate(DateTime.now().toDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Nurse(Integer id) {
		super(id);
	}
	
	public Nurse(Nurse orig) {
		super(orig);
	}
	
	@Override
	public String toString() {
		return getUser().getFirstName() + " " + getUser().getLastName();
	}
	
	public String getLink() {
		return "<a title=\"" + this + "\" href=\"nurse?id=" + getId() + "\">" + this + "</a>";
	}
	
	public DateTime getLastApp() {
		return null;
	}
	
	public DateTime getNextApp() {
		return null;
	}
	
	public int getPendAssesCount() {
		return getPendAsses().size();
	}
	
	public Collection<Appointment> getPendAsses() {
		ArrayList<Appointment> pendAsses = new ArrayList<Appointment>();
		for (Appointment appointment : getAppointments()) {
			if (appointment.isPending()) {
				pendAsses.add(appointment);
			}
		}
		return pendAsses;
	}
	
	public Collection<Appointment> getReviewables() {
		ArrayList<Appointment> col = new ArrayList<Appointment>();
		for (Appointment appointment : getAppointments()) {
			if (appointment.isReviewable()) {
				col.add(appointment);
			}
		}
		return col;
	}
	
	public Collection<Appointment> getPayables() {
		ArrayList<Appointment> col = new ArrayList<Appointment>();
		for (Appointment appointment : getAppointments()) {
			if (appointment.isPayable()) {
				col.add(appointment);
			}
		}
		return col;
	}
	
	public Collection<Appointment> getUnconfirmed() {
		ArrayList<Appointment> col = new ArrayList<Appointment>();
		for (Appointment appointment : getAppointments()) {
			if (!appointment.isNurseConfirmed()) {
				col.add(appointment);
			}
		}
		return col;
	}
	
	/**
   * Insert.
   * @throws Exception 
   */
  @Override
	public void insert() throws Exception {
  	insertPreCheck();
  	insertParents();
  	if (isNewInstance()) {
  		setId(getUser().getId());
  		getEntityManager().persist(this);
  	}
  	insertChildren();
  }
  
  public String getEmail() {
  	return getUser().getEmail();
  }
  
  Map<GeneralData, License> licenseHash;
  public License getLicense(GeneralData type) throws Exception {
  	if (licenseHash == null) {
  		fillLicenseHash();
  	}
  	License license = licenseHash.get(type);
  	if (license == null) {
  		license = createLicense(type);
  	}
  	return license;
  }
  
  private synchronized void fillLicenseHash() {
  	if (licenseHash == null) {
  		licenseHash = new HashMap<GeneralData, License>();
  		for (License license : getLicenses()) {
  			licenseHash.put(license.getLicType(), license);
    	}
  	}
  }
  
  private synchronized License createLicense(GeneralData type) throws Exception {
  	License license = licenseHash.get(type);
  	if (license == null) {
  		license = new License().setLicType(type).setNurse(this);
  		licenseHash.put(type, license);
  	}
  	return license;
  }

  @Override
	public JSONObject toJSON() throws JSONException {
		return super.toJSON()
				.put("user", getUser().toJSON());
	}

	public List<Pair<GeneralData, List<License>>> getAllLicenses() throws Exception {
		List<Pair<GeneralData, List<License>>> list = new ArrayList<Pair<GeneralData, List<License>>>();
		for (GeneralData category : GenData.LICENSE.get().getGeneralDatas()) {
			List<License> licenses = new ArrayList<License>();
			for (GeneralData type : category.getGeneralDatas()) {
				licenses.add(getLicense(type));
			}
			list.add(new Pair<GeneralData, List<License>>(category, licenses));
		}
		return list;
	}

	public List<Notification<?>> getNotifications() {
		List<Notification<?>> notifications = new ArrayList<Notification<?>>();
		for (License license : getLicenses()) {
			notifications.addAll(license.getNotifications());
		}
		return notifications;
	}
	
	public static List<Notification<?>> getAllNotifications() {
		List<Notification<?>> notifications = new ArrayList<Notification<?>>();
		for (Nurse nurse : getAllActive()) {
			notifications.addAll(nurse.getNotifications());
		}
		return notifications;
	}
	
	public static List<Notification<?>> getAllNotifications(int year, int month) {
		Pair<DateTime, DateTime> range = Calculate.getCalMonthRange(year, month);
		DateTime start = range.getLeft();
		DateTime end = range.getRight();
		List<Notification<?>> notifications = new ArrayList<Notification<?>>();
		for (License license : License.getCollection(
				"SELECT o FROM License o WHERE o.EXPIRATION_DATE >= ?1 AND o.EXPIRATION_DATE < ?2",
				start.toDate(), end.toDate())) {
			if ((license.isExpired() || license.isWarning()) &&
					license.getNurse().getStatus() == GenData.NURSE_STATUS_ACTIVE.get()) {
				notifications.addAll(license.getNotifications());
			}
		}
		return notifications;
	}

	public List<Appointment> getAppointments(int year, int month) {
		if (isNewInstance()) {
			return getAppointments();
		}
		Pair<DateTime, DateTime> range = Calculate.getCalMonthRange(year, month);
		DateTime start = range.getLeft();
		DateTime end = range.getRight();
		return Appointment.getCollection(
				"SELECT o FROM Appointment o WHERE o.START >= ?1 AND o.START < ?2 AND o.NURSE_ID = ?3",
				start, end, getId());
	}
}
