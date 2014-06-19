package com.digitald4.common.model;

import com.digitald4.common.util.FormatText;

public enum GenData {
	UserType(null, 1), 
	UserType_Admin(UserType, 1),
	UserType_Standard(UserType, 2),
	TransType(null, 100),
	TransType_Insert(TransType, 1),
	TransType_Update(TransType, 2),
	TransType_Delete(TransType, 3),
	FileType(null, 101),
	FileType_Misc(FileType, 1),
	FileType_PDF(FileType, 2),
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
