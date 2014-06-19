package com.digitald4.iis.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.digitald4.common.component.Notification;
import com.digitald4.common.model.FileAttachable;
import com.digitald4.iis.dao.LicenseDAO;

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
@Table(schema="iis",name="license")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM License o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM License o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM License o"),//AUTO-GENERATED
	@NamedQuery(name = "findByNurse", query="SELECT o FROM License o WHERE o.NURSE_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM license o WHERE o.ID=?"),//AUTO-GENERATED
})
public class License extends LicenseDAO implements FileAttachable {
	private List<Notification<License>> notifications;
	
	public License() {
	}
	public License(Integer id) {
		super(id);
	}
	public License(License orig) {
		super(orig);
	}
	
	@Override
	public String toString() {
		return getLicType().getName();
	}
	
	@Override
	public License setExpirationDate(Date expirationDate) throws Exception {
		notifications = null;
		return super.setExpirationDate(expirationDate);
	}
	
	public boolean showExp() {
		return getLicType().getDataAttribute("expires") != Boolean.FALSE;
	}
	
	public String getDataFileHTML() {
		if (getDataFileId() != null) {
			return DOWNLOAD_LINK.replaceAll("__className__", getClass().getName()).replaceAll("__id__", "" + getId());
		} else if (!isNewInstance()) {
			return "<button onClick=\"showLicUploadDialog('" + getClass().getName() + "', " + getId() + ", " + getLicTypeId() + "); return false;\">Upload File</button>";
		}
		return "";
	}
	
	@Override
	public JSONObject toJSON() throws JSONException {
		return super.toJSON().put("id", getLicTypeId())
				.put("showExp", showExp())
				.put("dataFileHTML", getDataFileHTML());
	}
	
	public List<Notification<License>> getNotifications() {
		if (notifications == null) {
			notifications = new ArrayList<Notification<License>>();
			Date expDate = getExpirationDate();
			if (expDate != null) {
				notifications.add(new Notification<License>("Expiration of " + toString() + ": " + getNurse(),
						expDate, Notification.Type.ERROR, this));
				notifications.add(new Notification<License>("30 days till " + toString() + " Expiration: " + getNurse(),
						new DateTime(expDate).minusDays(30).toDate(), Notification.Type.WARNING, this));
			}
		}
		return notifications;
	}
}
