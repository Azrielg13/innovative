package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Nurse.DistanceNurse;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Comparator;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class NurseStore extends GenericStore<Nurse> {

	@Inject
	public NurseStore(Provider<DAO> daoProvider) {
		super(Nurse.class, daoProvider);
	}

	public QueryResult<DistanceNurse> getCloset(double latitude, double longitude, int pageSize, int pageToken) {
		ImmutableList<DistanceNurse> nurses = list(new Query()).getResults()
				.stream()
				.map(nurse -> new DistanceNurse(latitude, longitude, nurse))
				.sorted(Comparator.comparingDouble(DistanceNurse::getDistance).thenComparing(n -> n.getNurse().getFullName()))
				.collect(toImmutableList());

		return QueryResult.of(
				nurses
						.stream()
						.skip(pageToken)
						.limit(pageSize)
						.collect(toImmutableList()),
				nurses.size(),
				Query.forValues(null, null, pageSize, pageToken));
	}
}