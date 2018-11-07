package com.digitald4.iis.server;

import com.digitald4.common.proto.DD4Protos.Query;
import com.digitald4.common.server.service.DualProtoService;
import com.digitald4.common.server.service.JSONServiceImpl;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.storage.Store;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.ProtoUtil;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISUIProtos.ClosestNursesRequest;
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;

@Api(
		name = "nurses",
		version = "v1",
		namespace = @ApiNamespace(
				ownerDomain = "iis.digitald4.com",
				ownerName = "iis.digitald4.com"
		),
		// [START_EXCLUDE]
		issuers = {
				@ApiIssuer(
						name = "firebase",
						issuer = "https://securetoken.google.com/fantasy-predictor",
						jwksUri = "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
		}
		// [END_EXCLUDE]
)
public class NurseService extends DualProtoService<NurseUI, Nurse> {

	private final Store<Nurse> nurseStore;
	NurseService(Store<Nurse> nurseStore) {
		super(NurseUI.class, nurseStore);
		this.nurseStore = nurseStore;
	}

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "closest")
	public QueryResult<NurseUI> listClosest(ClosestNursesRequest request) throws DD4StorageException {
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

	private static final Comparator<NurseUI> compareByDistance = (n1, n2) -> {
		int ret = Double.compare(n1.getDistance(), n2.getDistance());
		return ret != 0 ? ret : n1.getFullName().compareTo(n2.getFullName());
	};

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
}
