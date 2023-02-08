package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Nurse.DistanceNurse;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Comparator;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class NurseStore extends GenericLongStore<Nurse> {

	@Inject
	public NurseStore(Provider<DAO> daoProvider) {
		super(Nurse.class, daoProvider);
	}

	public QueryResult<DistanceNurse> getCloset(double lat, double lon, int pageSize, int pageToken) {
		ImmutableList<DistanceNurse> nurses = list(Query.forList()).getItems()
				.stream()
				.map(nurse -> new DistanceNurse(lat, lon, nurse))
				.sorted(
						Comparator
								.comparingDouble(DistanceNurse::getDistance)
								.thenComparing(n -> n.getNurse().fullName()))
				.collect(toImmutableList());

		return QueryResult.of(
				nurses
						.stream()
						.skip(pageToken)
						.limit(pageSize)
						.collect(toImmutableList()),
				nurses.size(),
				Query.forList(null, null, pageSize, pageToken));
	}
}
