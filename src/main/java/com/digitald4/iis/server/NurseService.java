package com.digitald4.iis.server;

import com.digitald4.common.proto.DD4Protos.Query;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.JSONServiceImpl;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.storage.Store;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.ProtoUtil;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISUIProtos.ClosestNursesRequest;
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class NurseService extends DualProtoService<NurseUI, Nurse> {

	private final Store<Nurse> nurseStore;
	NurseService(Store<Nurse> nurseStore) {
		super(NurseUI.class, nurseStore);
		this.nurseStore = nurseStore;
	}

	private QueryResult<NurseUI> listClosest(ClosestNursesRequest request) throws DD4StorageException {
		double lat = request.getLatitude();
		double lon = request.getLongitude();
		List<NurseUI> nurses = nurseStore.list(Query.getDefaultInstance()).getResults()
				.stream()
				.map(nurse -> getConverter().apply(nurse).toBuilder()
						.setDistance(
								Calculate.round(
										Calculate.distance(
												lat, lon, nurse.getAddress().getLatitude(), nurse.getAddress().getLongitude()),
										1))
						.build())
				.sorted(compareByDistance)
				.collect(Collectors.toList());
		return new QueryResult<>(
				nurses
						.stream()
						.skip(request.getPageToken())
						.limit(request.getPageSize())
						.collect(Collectors.toList()),
				nurses.size());
	}

	protected static class NurseJSONService extends JSONServiceImpl<NurseUI> {

		private final NurseService nurseService;
		public NurseJSONService(NurseService nurseService) {
			super(nurseService, true);
			this.nurseService = nurseService;
		}

		@Override
		public JSONObject performAction(String action, JSONObject jsonRequest) {
			if (action.equals("closest")) {
				return ProtoUtil.toJSON(
						nurseService.listClosest(ProtoUtil.toProto(ClosestNursesRequest.getDefaultInstance(), jsonRequest)));
			} else {
				return super.performAction(action, jsonRequest);
			}
		}
	}

	private static final Comparator<NurseUI> compareByDistance = (n1, n2) -> {
		int ret = Double.compare(n1.getDistance(), n2.getDistance());
		return ret != 0 ? ret : n1.getFullName().compareTo(n2.getFullName());
	};
}
