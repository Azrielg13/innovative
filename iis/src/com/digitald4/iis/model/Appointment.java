package com.digitald4.iis.model;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digitald4.common.component.CalEvent;
import com.digitald4.common.model.FileAttachable;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.dao.AppointmentDAO;
@Entity
@Table(schema="iis",name="appointment")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Appointment o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Appointment o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Appointment o"),//AUTO-GENERATED
	@NamedQuery(name = "findByPatient", query="SELECT o FROM Appointment o WHERE o.PATIENT_ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findByNurse", query="SELECT o FROM Appointment o WHERE o.NURSE_ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findByPaystub", query="SELECT o FROM Appointment o WHERE o.PAYSTUB_ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findByInvoice", query="SELECT o FROM Appointment o WHERE o.INVOICE_ID=?1"),//AUTO-GENERATED
	
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM appointment o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Appointment extends AppointmentDAO implements CalEvent, FileAttachable {

	public Appointment(){
	}

	public Appointment(Integer id){
		super(id);
	}

	public Appointment(Appointment orig){
		super(orig);
	}

	public Object getAssessmentValue(int assessmentId) throws Exception {
		return getAssessmentValue(GeneralData.getInstance(assessmentId));
	}

	public Object getAssessmentValue(GeneralData assessment) throws Exception {
		return getAssessmentEntry(assessment).getValue();
	}

	@Override
	public Object getPropertyValue(String property) {
		if (Character.isDigit(property.charAt(0))) {
			try {
				return getAssessmentValue(Integer.parseInt(property));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else if (property.equalsIgnoreCase("START_DATE")) {
			return getStartDate();
		} else if (property.equalsIgnoreCase("START_TIME")) {
			return getStartTime();
		} else if (property.equalsIgnoreCase("END_DATE")) {
			return getEndDate();
		} else if (property.equalsIgnoreCase("END_TIME")) {
			return getEndTime();
		}
		return super.getPropertyValue(property);
	}

	@Override
	public Appointment setPropertyValue(String property, String value) throws Exception {
		property = formatProperty(property);
		if (Character.isDigit(property.charAt(0))) {
			setAssessmentEntry(GeneralData.getInstance(Integer.parseInt(property)), value);
		} else if (property.equalsIgnoreCase("START_DATE")) {
			setStartDate(FormatText.USER_DATE.parse(value));
		} else if (property.equalsIgnoreCase("START_TIME")) {
			setStartTime(new Time(FormatText.USER_TIME.parse(value).getTime()));
		} else if (property.equalsIgnoreCase("END_DATE")) {
			setEndDate(FormatText.USER_DATE.parse(value));
		} else if (property.equalsIgnoreCase("END_TIME")) {
			setEndTime(new Time(FormatText.USER_TIME.parse(value).getTime()));
		} else if (property.equalsIgnoreCase("TIME_IN")) {
			setTimeInD(new DateTime(FormatText.USER_TIME.parse(value)));
		} else if (property.equalsIgnoreCase("TIME_OUT")) {
			setTimeOutD(new DateTime(FormatText.USER_TIME.parse(value)));
		} else {
			try {
				super.setPropertyValue(property, value);
			} catch (IllegalArgumentException e) {
				if (!e.getMessage().contains("No enum")) {
					throw e;
				}
				super.setPropertyValue(property + "_D", value);
			}
		}
		return this;
	}

	public AssessmentEntry getAssessmentEntry(GeneralData assessment) throws Exception {
		for (AssessmentEntry ae : getAssessmentEntrys()) {
			if (ae.getAssessment() == assessment) {
				return ae;
			}
		}
		return new AssessmentEntry().setAppointment(this).setAssessment(assessment);
	}

	public Appointment setAssessmentEntry(GeneralData assessment, String value) throws Exception {
		AssessmentEntry ae = getAssessmentEntry(assessment).setValue(value);
		if (ae.isNewInstance()) {
			addAssessmentEntry(ae);
		} else {
			ae.save();
		}
		return this;
	}

	public static List<Appointment> getPending() {
		DateTime now = DateTime.now();
		List<Appointment> pot = getCollection(new String[]{"" + PROPERTY.CANCELLED, "" + PROPERTY.ASSESSMENT_COMPLETE}, false, false);
		for (Appointment app : pot) {
			if (app.getStart().isAfter(now)) {
				return pot.subList(0, pot.indexOf(app));
			}
		}
		return pot;
	}

	public static List<Appointment> getPending(Vendor vendor) {
		DateTime now = DateTime.now();
		List<Appointment> pending = new ArrayList<Appointment>();
		for (Appointment app : getCollection(new String[]{"" + PROPERTY.CANCELLED, "" + PROPERTY.ASSESSMENT_COMPLETE}, false, false)) {
			if (app.getStart().isAfter(now)) {
				break;
			}
			if (app.getPatient().getVendor() == vendor) {
				pending.add(app);
			}
		}
		return pending;
	}

	public static List<Appointment> getReviewables() {
		return getCollection(new String[]{"" + PROPERTY.CANCELLED, "" + PROPERTY.ASSESSMENT_COMPLETE, "" + PROPERTY.ASSESSMENT_APPROVED}, false, true, false);
	}

	public static List<Appointment> getPayables() {
		return getCollection(new String[]{"" + PROPERTY.CANCELLED, "" + PROPERTY.ASSESSMENT_APPROVED, "" + PROPERTY.PAYSTUB_ID}, false, true, null);
	}

	public static List<Appointment> getBillables() {
		return getCollection(new String[]{"" + PROPERTY.CANCELLED, "" + PROPERTY.ASSESSMENT_APPROVED, "" + PROPERTY.INVOICE_ID}, false, true, null);
	}

	@Override
	public boolean isActiveOnDay(Date date) {
		return isActiveBetween(new DateTime(date), new DateTime(date).plusDays(1));
	}

	@Override
	public String getTitle() {
		return "" + getPatient();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isActiveBetween(DateTime start, DateTime end) {
		start = start.minusMillis(1);
		DateTime st = getStart();
		DateTime et = getEnd();
		// Did this event start any time between these periods or did these period start any time during this event
		return (start.isBefore(st) && end.isAfter(st) || st.isBefore(start) && et.isAfter(start));
	}

	private static int dataPoints = 0;
	public static int getDataPointTotal() throws Exception {
		if (dataPoints == 0) {
			for (GeneralData cat : GenData.ASS_CAT.get().getGeneralDatas()) {
				dataPoints += cat.getGeneralDatas().size();
			}
		}
		return dataPoints;
	}

	public int getPercentComplete() throws Exception {
		return getAssessmentEntrys().size() * 100 / getDataPointTotal();
	}

	@Override
	public Appointment setStart(DateTime start) throws Exception {
		super.setStart(start);
		checkEndDate();
		return this;
	}

	@Override
	public Appointment setEnd(DateTime end) throws Exception {
		super.setEnd(end);
		checkEndDate();
		return this;
	}

	public Date getStartDate() {
		DateTime start = getStart();
		if (start != null) {
			return start.toDate();
		}
		return null;
	}

	public void setStartDate(Date date) throws Exception {
		DateTime start = getStart();
		if (start == null) {
			setStart(new DateTime(date));
		} else {
			setStart(new DateTime(date).withMillisOfDay(start.getMillisOfDay()));
		}
	}

	public Time getStartTime() {
		DateTime start = getStart();
		if (start != null) {
			return new Time(start.getMillis());
		}
		return null;
	}

	public void setStartTime(Time time) throws Exception {
		DateTime start = getStart();
		if (start != null) {
			setStart(start.withMillisOfDay(new DateTime(time.getTime()).getMillisOfDay()));
		} else {
			setStart(new DateTime(time.getTime()));
		}
	}

	public Date getEndDate() {
		DateTime end = getEnd();
		if (end != null) {
			return end.toDate();
		}
		return null;
	}

	private void setEndDate(Date date) throws Exception {
		DateTime end = getEnd();
		if (end == null) {
			setEnd(new DateTime(date));
		} else {
			setEnd(new DateTime(date).withMillisOfDay(end.getMillisOfDay()));
		}
	}

	public Time getEndTime() {
		DateTime end = getEnd();
		if (end != null) {
			return new Time(end.getMillis());
		}
		return null;
	}

	private void checkEndDate() throws Exception {
		DateTime start = getStart();
		if (start == null) {
			return;
		}
		DateTime end = getEnd();
		if (end == null) {
			setEnd(start.plusHours(3));
		} else if (end.isBefore(start)) {
			setEndDate(start.plusDays(1).toDate());
		} else if ((end.getMillis() - start.getMillis()) > Calculate.ONE_DAY) {
			setEndDate(start.toDate());
		}
	}

	public void setEndTime(Time time) throws Exception {
		DateTime end = getEnd();
		if (end == null) {
			setEnd(new DateTime(time.getTime()));
		} else {
			setEnd(end.withMillisOfDay(new DateTime(time.getTime()).getMillisOfDay()));
		}
	}

	@Override
	public int getDuration() {
		DateTime start = getStart();
		DateTime end = getEnd();
		if (start != null && end != null) {
			return (int)(end.getMillis() - start.getMillis()) / 60000;
		}
		return 0;
	}

	public Appointment setDuration(int duration) throws Exception {
		setEnd(getStart().plusMinutes(duration));
		return this;
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Appointment) {
			Appointment app = (Appointment)o;
			int ret = getStart().compareTo(app.getStart());
			if (ret == 0) {
				ret = getEnd().compareTo(app.getEnd());
			}
			if (ret != 0) {
				return ret;
			}
		}
		return super.compareTo(o);
	}

	public boolean isPending() {
		return System.currentTimeMillis() > getStart().getMillis() && !isAssessmentComplete() && !isCancelled();
	}

	public boolean isReviewable() {
		return isAssessmentComplete() && !isAssessmentApproved();
	}

	public boolean isPayable() {
		return isAssessmentApproved() && !isPaid();
	}

	public boolean isBillable() {
		return isAssessmentApproved() && !isBilled();
	}

	public boolean isPaid() {
		return getPaystubId() != null;
	}

	public JSONObject toAssessmentJSON() throws Exception {
		JSONObject json = toJSON();
		JSONArray cats = new JSONArray();
		for (GeneralData cat : GenData.ASS_CAT.get().getGeneralDatas()) {
			JSONObject catJson = cat.toJSON();
			JSONArray entries = new JSONArray();
			for (GeneralData value : cat.getGeneralDatas()) {
				entries.put(getAssessmentEntry(value));
			}
			catJson.put("entries", entries);
			cats.put(catJson);
		}
		json.put("categories", cats);
		return json;
	}
	
	public DateTime getTimeIn() {
		DateTime time = getTimeInD();
		return time != null ? time : getStart();
	}
	
	public DateTime getTimeOut() {
		DateTime time = getTimeOutD();
		return time != null ? time : getEnd();
	}

	public double getLoggedHours() {
		if (getTimeOut() == null || getTimeIn() == null) {
			return 0;
		}
		long diff = getTimeOut().getMinuteOfDay() - getTimeIn().getMinuteOfDay();
		diff = (Math.round(diff / 15.0) * 15);
		double hours = diff / 60.0;
		if (hours < 0) {
			hours += 24;
		}
		return hours;
	}

	public double getBilledHours() {
		double hours = getBilledHoursD();
		if (hours > 0) {
			return hours;
		}
		hours = getLoggedHours();
		GeneralData bt = getBillingType();
		if (bt == GenData.ACCOUNTING_TYPE_FIXED.get()) {
			return 0;
		} else if (bt == GenData.ACCOUNTING_TYPE_SOC_2HR.get()) {
			hours -= 2;
			if (hours < 0) {
				return 0;
			}
		} else if (bt == GenData.ACCOUNTING_TYPE_ROC_2HR.get()) {
			hours -= 2;
			if (hours < 0) {
				return 0;
			}
		}
		return hours;
	}

	public double getPayHours() {
		double hours = getPayHoursD();
		if (hours > 0) {
			return hours;
		}
		hours = getLoggedHours();
		GeneralData pt = getPayingType();
		if (pt == GenData.ACCOUNTING_TYPE_FIXED.get()) {
			return 0;
		} else if (pt == GenData.ACCOUNTING_TYPE_SOC_2HR.get()) {
			hours -= 2;
			
		} else if (pt == GenData.ACCOUNTING_TYPE_ROC_2HR.get()) {
			hours -= 2;
		}
		if (hours < 0) {
			return 0;
		}
		return hours;
	}

	public double getPayMileageRate() {
		Nurse nurse = getNurse();
		if (getPayMileageRateD() > 0 || nurse == null) {
			return getPayMileageRateD();
		}
		return nurse.getMileageRate();
	}

	public boolean isStartOfCare() {
		// TODO Create a way to override this.
		return getPrevAppointment() == null;
	}

	public Appointment lockInPayment() throws Exception {
		setPayRateD(getPayRate());
		setPayMileageD(getPayMileage());
		setTimeInD(getTimeIn());
		setTimeOutD(getTimeOut());
		return setPayMileageRateD(getPayMileageRate());
	}

	@Override
	public Appointment setPaystub(Paystub paystub) throws Exception {
		lockInPayment();
		return super.setPaystub(paystub);
	}

	public double getPayRate() {
		if (getPayRateD() != 0) {
			return getPayRateD();
		}
		Nurse nurse = getNurse();
		if (nurse == null) {
			return 0;
		}
		GeneralData pt = getPayingType();
		if (pt == GenData.ACCOUNTING_TYPE_FIXED.get()) {
			return 0;
		} else if (pt == GenData.ACCOUNTING_TYPE_SOC_2HR.get()) {
			return nurse.getPayRate2HrSoc();
		} else if (pt == GenData.ACCOUNTING_TYPE_ROC_2HR.get()) {
			return nurse.getPayRate2HrRoc();
		}
		return nurse.getPayRate();
	}

	public Appointment getPrevAppointment() {
		List<Appointment> appointments = getPatient().getAppointments();
		int index = appointments.indexOf(this);
		if (index > 0) {
			return appointments.get(index-1);
		}
		return null;
	}

	public Appointment lockInBilling() throws Exception {
		setBillingRateD(getBillingRate());
		return setBillingMileageRateD(getBillingMileageRate());
	}

	@Override
	public Appointment setInvoice(Invoice invoice) throws Exception {
		lockInBilling();
		return super.setInvoice(invoice);
	}

	public boolean isBilled() {
		return getInvoiceId() != null;
	}
	
	public double getBillingRate() {
		double br = 0;
		if (getBillingRateD() != 0) {
			return super.getBillingRateD();
		}
		GeneralData bt = getBillingType();
		if (bt == GenData.ACCOUNTING_TYPE_HOURLY.get()) {
			br = getPatient().getBillingRate();
			if (br == 0) {
				br = getPatient().getVendor().getBillingRate();
			}
		} if (bt == GenData.ACCOUNTING_TYPE_SOC_2HR.get()) {
			br = getPatient().getBillingRate2HrSoc();
			if (br == 0) {
				br = getPatient().getVendor().getBillingRate2HrSoc();
			}
		} else if (bt == GenData.ACCOUNTING_TYPE_ROC_2HR.get()) {
			br = getPatient().getBillingRate2HrRoc();
			if (br == 0) {
				br = getPatient().getVendor().getBillingRate2HrRoc();
			}
		}
		return br;
	}

	public GeneralData getBillingType() {
		GeneralData bt = getBillingTypeD();
		if (bt == null || bt == GenData.ACCOUNTING_TYPE_AUTO_DETECT.get()) {
			if (getLoggedHours() <= 2) {
				return isStartOfCare() ?
						GenData.ACCOUNTING_TYPE_SOC_2HR.get() : GenData.ACCOUNTING_TYPE_ROC_2HR.get();
			}
			return GenData.ACCOUNTING_TYPE_HOURLY.get();
		}
		return bt;
	}

	public GeneralData getPayingType() {
		GeneralData bt = getPayingTypeD();
		if (bt == null || bt == GenData.ACCOUNTING_TYPE_AUTO_DETECT.get()) {
			if (getLoggedHours() <= 2) {
				return isStartOfCare() ?
						GenData.ACCOUNTING_TYPE_SOC_2HR.get() : GenData.ACCOUNTING_TYPE_ROC_2HR.get();
			}
			return GenData.ACCOUNTING_TYPE_HOURLY.get();
		}
		return bt;
	}
	
	public Appointment setPayingType(GeneralData payingType) throws Exception {
		return setPayingTypeD(payingType);
	}

	public Appointment setBillingType(GeneralData billingType) throws Exception {
		return setBillingTypeD(billingType);
	}

	public double getBillingFlat() {
		double bf = getBillingFlatD();
		if (bf > 0) {
			return bf;
		}
		GeneralData bt = getBillingType();
		if (bt == GenData.ACCOUNTING_TYPE_FIXED.get()) {
			bf = getPatient().getBillingFlat();
			if (bf == 0) {
				bf = getPatient().getVendor().getBillingFlat();
			}
		} else if (bt == GenData.ACCOUNTING_TYPE_SOC_2HR.get()) {
			bf = getPatient().getBillingFlat2HrSoc();
			if (bf == 0) {
				bf = getPatient().getVendor().getBillingFlat2HrSoc();
			}
		} else if (bt == GenData.ACCOUNTING_TYPE_ROC_2HR.get()) {
			bf = getPatient().getBillingFlat2HrRoc();
			if (bf == 0) {
				bf = getPatient().getVendor().getBillingFlat2HrRoc();
			}
		}
		return bf;
	}

	public double getPayFlat() {
		double bf = getPayFlatD();
		if (bf > 0) {
			return bf;
		}
		Nurse nurse = getNurse();
		if (nurse == null) {
			return 0;
		}
		GeneralData pt = getPayingType();
		if (pt == GenData.ACCOUNTING_TYPE_FIXED.get()) {
			return nurse.getPayFlat();
		} else if (pt == GenData.ACCOUNTING_TYPE_SOC_2HR.get()) {
			return nurse.getPayFlat2HrSoc();
		} else if (pt == GenData.ACCOUNTING_TYPE_ROC_2HR.get()) {
			return nurse.getPayFlat2HrRoc();
		}
		return bf;
	}

	public double getBillingMileageRate() {
		if (isBilled() || getBillingMileageRateD() > 0) {
			return getBillingMileageRateD();
		}
		double mr = getPatient().getMileageRate();
		if (mr == 0) {
			mr = getPatient().getVendor().getMileageRate();
		}
		return mr;
	}

	public double getBillingMileage() {
		double vm = getBillingMileageD();
		if (vm >= 0) {
			return vm;
		}
		if (getBillingMileageRate() >= 0) {
			return getPayMileage();
		}
		return 0;
	}

	public double getBillingMileageTotal() {
		return getBillingMileageRate() * getBillingMileage();
	}

	public double getPayMileageTotal() {
		return getPayMileageRate() * getPayMileage();
	}

	public double getBillingTotal() {
		return Math.round((getBillingFlat() + getBilledHours() * getBillingRate() + getBillingMileageTotal()) * 100) / 100.0;
	}

	public double getPaymentTotal() {
		return Math.round((getPayFlat() + getPayHours() * getPayRate() + getPayMileageTotal()) * 100) / 100.0;
	}

	public double getGrossTotal() {
		return Math.round((getPayFlat() + getPayHours() * getPayRate()) * 100) / 100.0;
	}
	
	public Appointment getPrevSameNurseDayApp() {
		Nurse nurse = getNurse();
		if (nurse == null) {
			return null;
		}
		DateTime start = getStart();
		List<Appointment> appointments = nurse.getAppointments(start.getYear(), start.getMonthOfYear());
		TreeSet<Appointment> suiters = new TreeSet<Appointment>();
		for (Appointment appointment : appointments) {
			if (appointment != this && appointment.getStart().getDayOfYear() == start.getDayOfYear()
					&& appointment.getStart().isBefore(start)) {
				suiters.add(appointment);
			}
		}
		if (!suiters.isEmpty()) {
			return suiters.last();
		}
		return null;
	}

	public double getSelfPaidMileage() {
		Appointment app = getPrevSameNurseDayApp();
		if (app != null) {
			double selfPaid = app.getSelfPaidMileage() - app.getMileage();
			return Math.max(0, selfPaid);
		}
		return 20;
	}

	public double getPayMileage() {
		double pm = getPayMileageD();
		if (pm >= 0) {
			return pm;
		}
		pm = getBillingMileageD();
		if (pm >= 0) {
			return pm;
		}
		pm = getMileage() - getSelfPaidMileage();
		if (pm >= 0) {
			return pm;
		}
		return 0;
	}
	
	public String getDataFileHTML() {
		if (getDataFileId() != null) {
			return DOWNLOAD_LINK.replaceAll("__className__", getClass().getName()).replaceAll("__id__", "" + getId());
		} else if (!isNewInstance()) {
			return "<button onClick=\"showUploadDialog('" + getClass().getName() + "', " + getId() + ", uploadFile); return false;\">Upload File</button>";
		}
		return "";
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		return super.toJSON()
				.put("timeIn", FormatText.formatTime(getTimeIn()))
				.put("timeOut", FormatText.formatTime(getTimeOut()))
				.put("billedHours", getBilledHours())
				.put("billingFlat", getBillingFlat())
				.put("billingRate", getBillingRate())
				.put("billingMileage", getBillingMileage())
				.put("billingMileageRate", getBillingMileageRate())
				.put("billingTotal", FormatText.CURRENCY.format(getBillingTotal()))
				.put("paymentTotal", FormatText.CURRENCY.format(getPaymentTotal()))
				.put("payHours", getPayHours())
				.put("payFlat", getPayFlat())
				.put("payRate", getPayRate())
				.put("payMileage", getPayMileage())
				.put("payMileageRate", getPayMileageRate())
				.put("dataFileHTML", getDataFileHTML());
	}

	public Vendor getVendor() {
		return getPatient().getVendor();
	}

	public double getMileage() {
		return getMileageD();
	}

	public static Collection<Appointment> getAppointments(int year, int month) {
		DateTime start = DateTime.parse(year + "-" + month + "-01").minusDays(6);
		DateTime end = DateTime.parse(year + "-" + month + "-01").plusMonths(1).plusDays(6);
		return getCollection("SELECT o FROM Appointment o WHERE o.START >= ?1 AND o.START < ?2 AND o.CANCELLED = ?3",
				start, end, false);
	}
	
	public boolean isNurseConfirmed() {
		return getNurseConfirmation() != null;
	}

	public static Collection<Appointment> getUpComingUnconfirmed() {
		DateTime start = DateTime.now().minusDays(1);
		DateTime end = DateTime.now().plusDays(7);
		return getCollection("SELECT o FROM Appointment o WHERE o.START >= ?1 AND o.START < ?2 AND o.NURSE_CONFIRMATION = ?3",
				start, end, null);
	}

	public static Collection<Appointment> getUnconfirmed() {
		return getCollection("SELECT o FROM Appointment o WHERE o.NURSE_CONFIRMATION = ?3", (Object[])null);
	}
}
