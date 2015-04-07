package com.tblin.openconn.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.tblin.openconn.log.OpenConnLogger;
import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.bean.UserInfo;
import com.tencent.tauth.http.Callback;

class QZoneApiExecutor implements ApiExecutor {

	@Override
	public void getUserInfo(final ApiExecuteCallback callback, String... params) {
		if (isParamErr(params))
			return;
		Callback mCallback = new Callback() {

			@Override
			public void onSuccess(Object arg0) {
				String str = arg0.toString();
				OpenConnLogger.i("before " + str);
				UserInfo info = (UserInfo) arg0;
				JSONObject json = new JSONObject();
				try {
					json.put("name", info.getNickName());
					json.put("url50", info.getIcon_50());
					json.put("url100", info.getIcon_100());
					json.put("url30", info.getIcon_30());
					callback.onSucess(json.toString(), -1);
				} catch (JSONException e) {
					callback.onFail(ApiExecuteCallback.FAIL_QZ_API_EXCEPTION);
					e.printStackTrace();
				}
			}

			@Override
			public void onFail(int arg0, String arg1) {
				callback.onFail(arg0);
			}

			@Override
			public void onCancel(int arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		TencentOpenAPI.userInfo(params[2], params[1], params[0], mCallback);
	}

	@Override
	public void updateStatuses(ApiExecuteCallback callback, String... params) {
		// TODO Auto-generated method stub

	}

	private boolean isParamErr(String... strings) {
		return strings == null || strings.length != 3 || strings[0] == null
				|| strings[1] == null || strings[2] == null;
	}

}
