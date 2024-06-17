package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;
import static java.util.Comparator.comparingDouble;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.Employee.Status;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Nurse.DistanceNurse;
import com.google.common.collect.ImmutableList;

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
		ImmutableList<DistanceNurse> nurses = list(Query.forList(Filter.of("status", Status.Active))).getItems()
				.stream()
				.map(nurse -> new DistanceNurse(lat, lon, nurse))
				.sorted(comparingDouble(DistanceNurse::getDistance).thenComparing(n -> n.getNurse().fullName()))
				.collect(toImmutableList());

		return QueryResult.of(
				nurses.stream().skip(pageToken).limit(pageSize).collect(toImmutableList()),
				nurses.size(),
				Query.forList(null, null, pageSize, pageToken));
	}

	@Override
	protected Iterable<Nurse> postprocess(Iterable<Nurse> entities) {
		// After we do an update to a nurse we should migrate the licenses update name and status incase those changed.
		stream(super.postprocess(entities)).forEach(nurse ->
				licenseStore.create(licenseStore.list(Query.forList(Filter.of("NurseId", nurse.getId()))).getItems()));

		return entities;
	}
}
