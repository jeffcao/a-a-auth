package com.tblin.openconn.api;

import java.io.IOException;

import com.tblin.openconn.log.OpenConnLogger;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

class SinaWeiboApiExecutor implements ApiExecutor {

	@Override
	public void getUserInfo(final ApiExecuteCallback callback, String... params) {
		if (isParamErr(params))
			return;
		Oauth2AccessToken token = new Oauth2AccessToken(params[0], params[1]);
		UsersAPI userApi = new UsersAPI(token);
		RequestListener lsnr = new RequestListener() {

			@Override
			public void onIOException(IOException e) {
				OpenConnLogger.i(e.getMessage());
				callback.onFail(ApiExecuteCallback.FAIL_SWB_IOEXCEPTION);
			}

			@Override
			public void onError(WeiboException e) {
				OpenConnLogger.i(e.getMessage());
				callback.onFail(ApiExecuteCallback.FAIL_SWB_WB_EXCEPTION);
			}

			@Override
			public void onComplete(String response) {
				callback.onSucess(response, -1);
			}
		};
		try {
			long uid = Long.parseLong(params[2]);
			userApi.show(uid, lsnr);
		} catch (NumberFormatException e) {

		}

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
