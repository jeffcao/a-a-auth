package com.tblin.openconn.oauth;

import com.tblin.openconn.OpenPlatform;

public class QZoneOAuthRecord extends TencentOAuthRecord {

	public QZoneOAuthRecord(String clientId, String openId, String accessToken) {
		super(clientId, openId, accessToken);
	}

	@Override
	public OpenPlatform getPlatform() {
		return OpenPlatform.QZONE;
	}

}
