package com.tblin.openconn.api;

import com.tblin.openconn.OpenPlatform;

public class GeneralApiExecutor {

	/**
	 * 获取用户的基本信息
	 * 
	 * @param platform
	 * @param callback
	 * @param params <li>
	 *            腾讯微博：open id, client id, access token <li>
	 *            QQ 登录：open id, client id, access token <li>
	 *            新浪微博: access_token, expires_in, uid
	 */
	public static void getUserInfo(OpenPlatform platform,
			ApiExecuteCallback callback, String... params) {
		ApiExecutor executor = ApiExecutorFactory.getExecutor(platform);
		if (executor != null)
			executor.getUserInfo(callback, params);
	}

	public static void updateStatuses(OpenPlatform platform,
			ApiExecuteCallback callback, String... params) {
		ApiExecutor executor = ApiExecutorFactory.getExecutor(platform);
		if (executor != null)
			executor.updateStatuses(callback, params);
	}

}
