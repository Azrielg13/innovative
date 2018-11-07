package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4Protos;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.storage.UserStore;
import com.digitald4.iis.proto.IISProtos.Nurse;
import javax.inject.Provider;

public class NurseStore extends GenericStore<Nurse> {

	private final UserStore userStore;

	public NurseStore(Provider<DAO> daoProvider, UserStore userStore) {
		super(Nurse.class, daoProvider);
		this.userStore = userStore;
	}

	@Override
	public Nurse create(Nurse nurse) throws DD4StorageException {
		return super.create(nurse.toBuilder()
				.setId(userStore.create(DD4Protos.User.newBuilder()
						.setTypeId(4)
						.setUsername(nurse.getUserName())
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
