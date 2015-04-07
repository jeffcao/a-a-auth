package com.tblin.openconn.api;

import com.tblin.openconn.OpenPlatform;

class ApiExecutorFactory {

	public static ApiExecutor getExecutor(OpenPlatform platform) {
		ApiExecutor executor = null;
		switch (platform) {
		case SINA_WEIBO:
			executor = new SinaWeiboApiExecutor();
			break;
		case TENCENT_WEIBO:
			executor = new TencentWeiboApiExecutor();
			break;
		case QZONE:
			executor = new QZoneApiExecutorV2();
			break;
		}
		return executor;
	}

}
