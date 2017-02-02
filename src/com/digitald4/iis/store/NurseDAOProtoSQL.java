package com.digitald4.iis.store;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.proto.DD4Protos;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.storage.UserStore;
import com.digitald4.iis.proto.IISProtos.Nurse;

/**
 * Created by eddiemay on 1/23/17.
 */
public class NurseDAOProtoSQL extends DAOProtoSQLImpl<Nurse> {

	private final UserStore userStore;

	public NurseDAOProtoSQL(DBConnector connector, UserStore userStore) {
		super(Nurse.class, connector, "V_NURSE");
		this.userStore = userStore;
	}

	@Override
	public Nurse create(Nurse nurse) throws DD4StorageException {
		return super.create(nurse.toBuilder()
				.setId(userStore.create(DD4Protos.User.newBuilder()
						// .setType(DD4Protos.User.UserType.STANDARD)
						.setUserName(nurse.getUserName())
						.setEmail(nurse.getEmail())
						.setFirstName(nurse.getFirstName())
						.setLastName(nurse.getLastName())
						.setDisabled(nurse.getDisabled())
						.setReadOnly(nurse.getReadOnly())
						.setNotes(nurse.getNotes())
						.build()).getId())
				.clearUserName()
				.clearEmail()
				.clearFirstName()
				.clearLastName()
				.clearFullName()
				.clearDisabled()
				.clearReadOnly()
				.clearNotes()
				.build());
	}
}
