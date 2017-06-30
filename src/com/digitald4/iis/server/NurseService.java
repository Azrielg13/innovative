package com.digitald4.iis.server;

import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.storage.Store;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISUIProtos.ClosestNursesRequest;
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import com.googlecode.protobuf.format.JsonFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NurseService extends DualProtoService<NurseUI, Nurse> {

	private final Store<Nurse> nurseStore;
	NurseService(Store<Nurse> nurseStore) {
		super(NurseUI.class, nurseStore);
		this.nurseStore = nurseStore;
	}

	@Override
	public Object performAction(String action, JSONObject jsonRequest)
			throws DD4StorageException, JSONException, JsonFormat.ParseException {
		if (action.equals("closest")) {
			return convertToJSON(listClosest(transformJSONRequest(ClosestNursesRequest.getDefaultInstance(), jsonRequest)));
		} else {
			return super.performAction(action, jsonRequest);
		}
	}

	private List<NurseUI> listClosest(ClosestNursesRequest request) throws DD4StorageException {
		double lat = request.getLatitude();
		double lon = request.getLongitude();
		return nurseStore.getAll().stream()
				.map(nurse -> getConverter().apply(nurse).toBuilder()
						.setDistance(Calculate.round(Calculate.distance(lat, lon,
								nurse.getAddress().getLatitude(), nurse.getAddress().getLongitude()), 1))
						.build())
				.sorted(compareByDistance)
				.limit(request.getLimit())
				.collect(Collectors.toList());
	}

	private static final Comparator<NurseUI> compareByDistance = (n1, n2) -> {
		int ret = Double.compare(n1.getDistance(), n2.getDistance());
		return ret != 0 ? ret : n1.getFullName().compareTo(n2.getFullName());
	};
}
