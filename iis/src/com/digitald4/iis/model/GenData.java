package com.digitald4.iis.model;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.util.FormatText;

public enum GenData {
	UserType(null, 1), 
	UserType_Admin(UserType, 1),
	UserType_Standard(UserType, 2),
	ASS_CAT(null, 2),
	ASS_CAT_VITAL(ASS_CAT, 1),
	DIANOSIS(null, 3),
	THERAPY_TYPE(null, 4),
	IV_ACCESS(null, 5),
	PATIENT_STATE(null, 8),
	PATIENT_STATE_PENDING(PATIENT_STATE, 1),
	PATIENT_STATE_ACTIVE(PATIENT_STATE, 2),
	PATIENT_STATE_DENIED(PATIENT_STATE, 3),
	PATIENT_STATE_CLOSED(PATIENT_STATE, 4),
	PATIENT_STATE_INACTIVE(PATIENT_STATE, 5),
	PATIENT_STATE_DISCHARGED(PATIENT_STATE, 6),
	PATIENT_STATE_WAITING_FOR_AUTHORIZATION(PATIENT_STATE, 7),
	PATIENT_STATE_VACATION(PATIENT_STATE, 8),
	PATIENT_STATE_HOSPITALIZED(PATIENT_STATE, 9),
	LICENSE(null, 9),
	NURSE_STATUS(null, 10),
	NURSE_STATUS_PENDING(NURSE_STATUS, 1),
	NURSE_STATUS_ACTIVE(NURSE_STATUS, 2),
	NURSE_STATUS_SUSPENDED(NURSE_STATUS, 3),
	NURSE_STATUS_INACTIVE(NURSE_STATUS, 4),
	PAYMENT_STATUS(null, 11),
	PAYMENT_STATUS_UNPAID(PAYMENT_STATUS, 1),
	PAYMENT_STATUS_PAID(PAYMENT_STATUS, 2),
	PAYMENT_STATUS_CANCELLED(PAYMENT_STATUS, 3),
	PAYMENT_STATUS_PARTIAL_PAID(PAYMENT_STATUS, 4),
	ACCOUNTING_TYPE(null, 12),
	ACCOUNTING_TYPE_AUTO_DETECT(ACCOUNTING_TYPE, 1),
	ACCOUNTING_TYPE_HOURLY(ACCOUNTING_TYPE, 2),
	ACCOUNTING_TYPE_FIXED(ACCOUNTING_TYPE, 3),
	ACCOUNTING_TYPE_SOC_2HR(ACCOUNTING_TYPE, 4),
	ACCOUNTING_TYPE_ROC_2HR(ACCOUNTING_TYPE, 5),
	DEDUCTION_TYPE(null, 13),
	DEDUCTION_TYPE_PRE_TAX(DEDUCTION_TYPE, 1),
	DEDUCTION_TYPE_PRE_TAX_401K(DEDUCTION_TYPE_PRE_TAX, 1),
	DEDUCTION_TYPE_PRE_TAX_HEALTH_CARE(DEDUCTION_TYPE_PRE_TAX, 2),
	DEDUCTION_TYPE_PRE_TAX_DENTAL(DEDUCTION_TYPE_PRE_TAX, 3),
	DEDUCTION_TYPE_TAX(DEDUCTION_TYPE, 2),
	DEDUCTION_TYPE_TAX_FEDERAL(DEDUCTION_TYPE_TAX, 1),
	DEDUCTION_TYPE_TAX_STATE(DEDUCTION_TYPE_TAX, 2),
	DEDUCTION_TYPE_POST_TAX(DEDUCTION_TYPE, 3),
	DEDUCTION_TYPE_POST_TAX_GROUP_TERM_LIFE(DEDUCTION_TYPE_POST_TAX, 1),
	PATIENT_STATUS(null, 14),
	PATIENT_STATUS_SOC(PATIENT_STATUS, 1),
	PATIENT_STATUS_FU(PATIENT_STATUS, 2),
	PATIENT_STATUS_ROUTINE(PATIENT_STATUS, 3),
	;
	
	private GenData group;
	private Integer inGroupId;
	private GeneralData instance;

	private GenData(GenData group, Integer inGroupId) {
		this.group = group;
		this.inGroupId = inGroupId;
	}
	
	public Integer getInGroupId() {
		return inGroupId;
	}
	
	public GeneralData get() {
		if (instance == null) {
			instance = GeneralData.getInstance(group == null ? null : group.get(), inGroupId);
			if (instance == null) {
				String name = this.toString();
				if (this.group != null) {
					name = name.substring(this.group.toString().length() + 1);
				}
				name = FormatText.toSpaced(FormatText.toUpperCamel(name));
				System.err.println("Missing General Data: " + this + " inserting as " + name);
				try {
					instance = new GeneralData().setName(name).setDescription(name).setGroup(group != null ? group.get() : null).setInGroupId(getInGroupId());
					instance.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		return instance;
	}
}
