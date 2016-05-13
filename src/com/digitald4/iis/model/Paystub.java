package com.digitald4.iis.model;
import java.util.ArrayList;
import java.util.List;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.iis.dao.PaystubDAO;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;
@Entity
@Table(schema="iis",name="paystub")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Paystub o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Paystub o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Paystub o"),//AUTO-GENERATED
	@NamedQuery(name = "findByNurse", query="SELECT o FROM Paystub o WHERE o.NURSE_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM paystub o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Paystub extends PaystubDAO {
	
	public Paystub() {
		try {
			setPayDate(DateTime.now().toDate());
			setGenerationTime(DateTime.now());
			setStatus(GenData.PAYMENT_STATUS_UNPAID.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Paystub(Integer id) {
		super(id);
	}
	
	public Paystub(Paystub orig) {
		super(orig);
	}
	
	public int compareTo(Object o) {
		if (o instanceof Paystub) {
			int ret = ((Paystub)o).getPayDate().compareTo(getPayDate());
			if (ret != 0) {
				return ret;
			}
			ret = ((Paystub)o).getId().compareTo(getId());
			if (ret != 0) {
				return ret;
			}
		}
		return super.compareTo(o);
	}
	
	public Paystub getPrev() {
		List<Paystub> paystubs = getNurse().getPaystubs();
		if (!paystubs.isEmpty()) {
			int index = paystubs.indexOf(this);
			if (index == -1) {
				return paystubs.get(paystubs.size() - 1);
			} else if (index > 0) {
				return paystubs.get(index - 1);
			}
		}
		return null;
	}
	
	public List<Deduction> getPreTaxDeductions() {
		List<Deduction> list = new ArrayList<Deduction>();
		for (Deduction deduction : getDeductions()) {
			if (deduction.isPreTax()) {
				list.add(deduction);
			}
		}
		return list;
	}
	
	public List<Deduction> getTaxDeductions() {
		List<Deduction> list = new ArrayList<Deduction>();
		for (Deduction deduction : getDeductions()) {
			if (deduction.isTax()) {
				list.add(deduction);
			}
		}
		return list;
	}
	
	public List<Deduction> getPostTaxDeductions() {
		List<Deduction> list = new ArrayList<Deduction>();
		for (Deduction deduction : getDeductions()) {
			if (deduction.isPostTax()) {
				list.add(deduction);
			}
		}
		return list;
	}
	
	public Paystub calc() throws Exception {
		int mileage = 0, mileageYTD = 0;
		double loggedHours = 0, payMileage = 0, nonTaxWages = 0, loggedHoursYTD = 0, payMileageYTD = 0, nonTaxWagesYTD = 0,
		grossPay = 0, preTaxDeduction = 0, taxable = 0, taxTotal = 0, postTaxDeduction = 0, netPay = 0,
		grossPayYTD = 0, preTaxDeductionYTD = 0, taxableYTD = 0, taxTotalYTD = 0, postTaxDeductionYTD = 0, netPayYTD = 0;
		
		// Starting YTD
		Paystub prev = getPrev();
		if (prev != null) {
			loggedHoursYTD = prev.getLoggedHoursYTD();
			mileageYTD = prev.getMileageYTD();
			payMileageYTD = prev.getPayMileageYTD();
			nonTaxWagesYTD = prev.getNonTaxWagesYTD();
			grossPayYTD = prev.getGrossPayYTD();
			preTaxDeductionYTD = prev.getPreTaxDeductionYTD();
			taxableYTD = prev.getTaxableYTD();
			taxTotalYTD = prev.getTaxTotalYTD();
			postTaxDeductionYTD = prev.getPostTaxDeductionYTD();
			netPayYTD = prev.getNetPayYTD();
		}
		
		// Gross
		for (Appointment appointment : getAppointments()) {
			loggedHours += appointment.getLoggedHours();
			mileage += appointment.getPayMileage();
			payMileage += appointment.getPayMileageTotal();
			grossPay += appointment.getGrossTotal();
		}
		nonTaxWages = payMileage;
		loggedHoursYTD += loggedHours;
		mileageYTD += mileage;
		nonTaxWagesYTD += nonTaxWages;
		payMileageYTD += payMileage;
		grossPayYTD += grossPay;
		
		// Pretax
		for (Deduction deduction : getPreTaxDeductions()) {
			deduction.calc(grossPay, prev);
			preTaxDeduction += deduction.getAmount();
		}
		preTaxDeductionYTD += preTaxDeduction;
		
		// Tax
		taxable = grossPay - preTaxDeduction;
		taxableYTD += taxable;
		for (Deduction deduction : getTaxDeductions()) {
			deduction.calc(taxable, prev);
			taxTotal += deduction.getAmount();
		}
		taxTotalYTD += taxTotal;
		
		//PostTax
		netPay = taxable - taxTotal;
		taxable = grossPay - preTaxDeduction;
		for (Deduction deduction : getPostTaxDeductions()) {
			deduction.calc(netPay, prev);
			postTaxDeduction += deduction.getAmount();
		}
		postTaxDeductionYTD += postTaxDeduction;
		
		// Net Pay
		netPay = netPay - postTaxDeduction + nonTaxWages;
		netPayYTD += netPay;
		
		setLoggedHours(loggedHours);
		setMileage(mileage);
		setPayMileage(payMileage);
		setNonTaxWages(nonTaxWages);
		setLoggedHoursYTD(loggedHoursYTD);
		setMileageYTD(mileageYTD);
		setPayMileageYTD(payMileageYTD);
		setGrossPay(grossPay);
		setPreTaxDeduction(preTaxDeduction);
		setTaxable(taxable);
		setTaxTotal(taxTotal);
		setPostTaxDeduction(postTaxDeduction);
		setNetPay(netPay);
		setGrossPayYTD(grossPayYTD);
		setPreTaxDeductionYTD(preTaxDeductionYTD);
		setTaxableYTD(taxableYTD);
		setTaxTotalYTD(taxTotalYTD);
		setPostTaxDeductionYTD(postTaxDeductionYTD);
		setNonTaxWagesYTD(nonTaxWagesYTD);
		setNetPayYTD(netPayYTD);
		return this;
	}

	public double getDeductionYTD(Deduction deduction) {
		for (Deduction deduc : getDeductions()) {
			if (deduction.getTypeId() == deduc.getTypeId()) {
				return deduc.getAmountYTD();
			}
		}
		return 0;
	}
	
	public DataAccessObject save() throws Exception {
		calc();
		return super.save();
	}
	
	@Override
	public void delete() throws Exception {
		for (Appointment app : new ArrayList<Appointment>(getAppointments())) {
			app.setPaystubId(null).save();
		}
		super.delete();
	}
}
