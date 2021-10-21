package com.digitald4.iis.server;

import com.digitald4.common.server.service.JSONServiceHelper;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.common.util.ProtoUtil;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.User;
import com.digitald4.iis.storage.NurseStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.*;
import javax.inject.Inject;
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
public class NurseService extends AdminService<Nurse> {

	private final NurseStore nurseStore;
	private final SessionStore<User> sessionStore;

	@Inject
	NurseService(NurseStore nurseStore, SessionStore<User> sessionStore) {
		super(nurseStore, sessionStore);
		this.nurseStore = nurseStore;
		this.sessionStore = sessionStore;
	}

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "closest")
	public QueryResult<Nurse.DistanceNurse> listClosest(
			@Named("latitude") double latitude, @Named("longitude") double longitude,
			@Named("pageSize") @DefaultValue("10") int pageSize, @Named("pageToken") int pageToken,
			@Named("idToken") String idToken) throws ServiceException {
		sessionStore.resolve(idToken, true);
		return nurseStore.getCloset(latitude, longitude, pageSize, pageToken);
	}

	protected static class NurseJSONService extends JSONServiceHelper<Nurse> {

		private final NurseService nurseService;
		public NurseJSONService(NurseService nurseService) {
			super(nurseService);
			this.nurseService = nurseService;
		}

		@Override
		public JSONObject performAction(String action, JSONObject jsonRequest) throws ServiceException {
			if (action.equals("closest")) {
				return ProtoUtil.toJSON(
						nurseService.listClosest(
								jsonRequest.optDouble("latitude"), jsonRequest.getDouble("longitude"),
								jsonRequest.optInt("pageSize"), jsonRequest.optInt("pageToken"),
								jsonRequest.getString("idToken")));
			}

			return super.performAction(action, jsonRequest);
		}
	}
}
