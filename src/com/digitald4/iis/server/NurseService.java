package com.digitald4.iis.server;

import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.storage.ListResponse;
import com.digitald4.common.storage.Store;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISUIProtos.ClosestNursesRequest;
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class NurseService extends DualProtoService<NurseUI, Nurse> {

	private final Store<Nurse> nurseStore;
	NurseService(Store<Nurse> nurseStore) {
		super(NurseUI.class, nurseStore);
		this.nurseStore = nurseStore;
	}

	@Override
	public JSONObject performAction(String action, JSONObject jsonRequest) {
		if (action.equals("closest")) {
			return listToJSON.apply(listClosest(transformJSONRequest(ClosestNursesRequest.getDefaultInstance(), jsonRequest)));
		} else {
			return super.performAction(action, jsonRequest);
		}
	}

	private ListResponse<NurseUI> listClosest(ClosestNursesRequest request) throws DD4StorageException {
		double lat = request.getLatitude();
		double lon = request.getLongitude();
		return ListResponse.<NurseUI>newBuilder()
				.addAllItems(nurseStore.list(ListRequest.getDefaultInstance()).getItemsList().stream()
						.filter(Nurse::hasAddress)
						.map(nurse -> getConverter().apply(nurse).toBuilder()
								.setDistance(Calculate.round(Calculate.distance(lat, lon,
										nurse.getAddress().getLatitude(), nurse.getAddress().getLongitude()), 1))
								.build())
						.sorted(compareByDistance)
						.skip(request.getPageToken())
						.limit(request.getPageSize())
						.collect(Collectors.toList()))
				.build();
	}

	private static final Comparator<NurseUI> compareByDistance = (n1, n2) -> {
		int ret = Double.compare(n1.getDistance(), n2.getDistance());
		return ret != 0 ? ret : n1.getFullName().compareTo(n2.getFullName());
	};
}
