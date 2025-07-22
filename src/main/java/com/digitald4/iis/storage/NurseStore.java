package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Comparator.comparingDouble;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.storage.Transaction.Op;
import com.digitald4.iis.model.Employee.Status;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Nurse.DistanceNurse;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

public class NurseStore extends GenericLongStore<Nurse> {
	private final LicenseStore licenseStore;

	@Inject
	public NurseStore(Provider<DAO> daoProvider, LicenseStore licenseStore) {
		super(Nurse.class, daoProvider);
		this.licenseStore = licenseStore;
	}

	public QueryResult<DistanceNurse> getCloset(double lat, double lon, int pageSize, int pageToken) {
		var distanceNurses = list(Query.forList(Filter.of("status", Status.Active))).getItems()
				.stream()
				.map(nurse -> new DistanceNurse(lat, lon, nurse))
				.sorted(comparingDouble(DistanceNurse::getDistance).thenComparing(n -> n.getNurse().fullName()))
				.collect(toImmutableList());

		return QueryResult.of(DistanceNurse.class,
				distanceNurses.stream().skip(pageToken).limit(pageSize).collect(toImmutableList()),
				distanceNurses.size(),
				Query.forList(null, null, null, pageSize, pageToken));
	}

	@Override
	protected Op<Nurse> postprocess(Op<Nurse> op) {
		Nurse nurse = op.getEntity();
		Nurse current = op.getCurrent();
		if (current != null && (!Objects.equals(nurse.fullName(), current.fullName()) || !Objects.equals(nurse.getStatus(), current.getStatus()))) {
			// If nurse name or status has changed, we will need to migrate the licenses.
			licenseStore.migrate(
					licenseStore.list(Query.forList(Filter.of("nurseId", nurse.getId()))).getItems());
		}

		return op;
	}
}
