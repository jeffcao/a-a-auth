package com.tblin.openconn.oauth;

import com.tblin.openconn.OpenPlatform;

public class TencentWeiboOAuthRecord extends TencentOAuthRecord {

	public TencentWeiboOAuthRecord(String clientId, String openId,
			String accessToken) {
		super(clientId, openId, accessToken);
	}

	@Override
	public OpenPlatform getPlatform() {
		return OpenPlatform.TENCENT_WEIBO;
	}

}
