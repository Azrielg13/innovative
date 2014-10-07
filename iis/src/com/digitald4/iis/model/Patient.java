package com.digitald4.iis.model;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.digitald4.common.component.Notification;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.dao.PatientDAO;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;
@Entity
@Table(schema="iis",name="patient")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Patient o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Patient o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Patient o"),//AUTO-GENERATED
	@NamedQuery(name = "findByVendor", query="SELECT o FROM Patient o WHERE o.BILLING_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM patient o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Patient extends PatientDAO {
	
	public Patient() {
		try {
			setReferralResolution(GenData.PATIENT_STATE_PENDING.get());
			setReferralDate(DateTime.now().toDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Patient(Integer id) {
		super(id);
	}
	
	public Patient(Patient orig) {
		super(orig);
	}
	
	public String getLink() {
		return "<a href=\"patient?id=" + getId() + "\">" + this + "</a>";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public static Collection<Patient> getByState(GeneralData state) {
		return getCollection(new String[]{"" + PROPERTY.REFERRAL_RESOLUTION_ID}, state.getId());
	}
	
	public Set<Pair<Nurse, Double>> getNursesByDistance() {
		Set<Pair<Nurse, Double>> nurses = new TreeSet<Pair<Nurse, Double>>();
		for (Nurse nurse : Nurse.getAllActive()) {
			nurses.add(new Pair<Nurse, Double>(nurse, Calculate.round(Calculate.distance(getLatitude(), getLongitude(), nurse.getLatitude(), nurse.getLongitude()), 1), Pair.Side.RIGHT));
		}
		return nurses;
	}

	public String getLastName() {
		int space = getName().trim().lastIndexOf(' ');
		if (space > 0) {
			return getName().substring(space + 1);
		}
		return getName();
	}
	
	public List<Appointment> getAppointments(int year, int month) {
		Pair<DateTime, DateTime> range = Calculate.getMonthRange(year, month);
		DateTime start = range.getLeft();
		DateTime end = range.getRight();
		return Appointment.getCollection(
				"SELECT o FROM Appointment o WHERE o.START >= ?1 AND o.START < ?2 AND o.PATIENT_ID = ?3",
				start, end, getId());
	}
	
	public static List<Notification<Patient>> getAllNotifications(int year, int month) {
		Pair<DateTime, DateTime> range = Calculate.getMonthRange(year, month);
		DateTime start = range.getLeft();
		DateTime end = range.getRight();
		List<Notification<Patient>> notifications = new ArrayList<Notification<Patient>>();
		for (Patient patient : getCollection("SELECT o FROM Patient o " +
				"WHERE o.EST_LAST_DAY_OF_SERVICE >= ?1 AND o.EST_LAST_DAY_OF_SERVICE < ?2 AND o.REFERRAL_RESOLUTION_ID = ?3",
				start.toDate(), end.toDate(), GenData.PATIENT_STATE_ACTIVE.get().getId())) {
			notifications.add(new Notification<Patient>("Last day of service for: " + patient,
					patient.getEstLastDayOfService(), Notification.Type.INFO, patient));
		}
		return notifications;
	}

	public List<Notification<?>> getNotifications(int year, int month) {
		Pair<DateTime, DateTime> range = Calculate.getMonthRange(year, month);
		DateTime start = range.getLeft();
		DateTime end = range.getRight();
		List<Notification<?>> notifications = new ArrayList<Notification<?>>();
		Date estLastDay = getEstLastDayOfService();
		if (estLastDay != null && estLastDay.getTime() > start.getMillis() && estLastDay.getTime() < end.getMillis())
			notifications.add(new Notification<Patient>("Last day of service for: " + this,
					getEstLastDayOfService(), Notification.Type.INFO, this));
		return notifications;
	}
}
