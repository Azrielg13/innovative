package com.digitald4.budget.model;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.util.FormatText;

public enum GenData {
	UserPortfolioRole(null, 300),
	UserPortfolioRole_OWNER(UserPortfolioRole, 1),
	AccountCategory(null, 301),
	AccountCategory_Bank_Account(AccountCategory, 1),
	AccountCategory_Utility(AccountCategory, 2),
	AccountCategory_Employeer(AccountCategory, 3),
	AccountCategory_Credit_Card(AccountCategory, 4)
	;
	
	private GenData group;
	private int inGroupId;
	private GeneralData instance;

	private GenData(GenData group, int inGroupId) {
		this.group = group;
		this.inGroupId = inGroupId;
	}
	
	public int getInGroupId() {
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
