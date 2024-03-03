package com.digitald4.iis.server;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.service.JSONServiceHelper;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.util.JSONUtil;
import com.digitald4.iis.model.Nurse;
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
		)
)
public class NurseService extends AdminService<Nurse> {
	private final NurseStore nurseStore;
	private final LoginResolver loginResolver;

	@Inject
	NurseService(NurseStore nurseStore, LoginResolver loginResolver) {
		super(nurseStore, loginResolver);
		this.nurseStore = nurseStore;
		this.loginResolver = loginResolver;
	}

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "closest")
	public QueryResult<Nurse.DistanceNurse> listClosest(
			@Named("latitude") double latitude, @Named("longitude") double longitude,
			@Named("pageSize") @DefaultValue("10") int pageSize,
			@Named("pageToken") @DefaultValue("0") int pageToken,
			@Named("idToken") String idToken) throws ServiceException {
		try {
			loginResolver.resolve(idToken, true);
			return nurseStore.getCloset(latitude, longitude, pageSize, pageToken);
		} catch (DD4StorageException e) {
			throw new ServiceException(e.getErrorCode(), e);
		}
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
				return JSONUtil.toJSON(
						nurseService.listClosest(
								jsonRequest.optDouble("latitude"), jsonRequest.getDouble("longitude"),
								jsonRequest.optInt("pageSize"), jsonRequest.optInt("pageToken"),
								jsonRequest.getString("idToken")));
			}

			return super.performAction(action, jsonRequest);
		}
	}
}
