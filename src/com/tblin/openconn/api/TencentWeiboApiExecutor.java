package com.tblin.openconn.api;

import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;

class TencentWeiboApiExecutor implements ApiExecutor {

	@Override
	public void getUserInfo(final ApiExecuteCallback callback, final String... params) {
		if (isParamErr(params))
			return;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				UserAPI userApi = new UserAPI(OAuthConstants.OAUTH_VERSION_2_A);
				OAuthV2 oAuth = new OAuthV2();
				oAuth.setOpenid(params[0]);
				oAuth.setClientId(params[1]);
				oAuth.setAccessToken(params[2]);
				try {
					String result = userApi.info(oAuth, "json");
					callback.onSucess(result, -1);
				} catch (Exception e) {
					callback.onFail(ApiExecuteCallback.FAIL_TWB_API_EXCEPTION);
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
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
