package com.tblin.openconn.oauth;

import com.tblin.openconn.OpenPlatform;

public class SinaWeiboOAuthRecord implements OAuthRecord {

	private String expiresIn;
	private String accessToken;
	private long uid;
	private String info;

	public SinaWeiboOAuthRecord(String expiresIn, String accessToken, long uid) {
		this.expiresIn = expiresIn;
		this.accessToken = accessToken;
		this.uid = uid;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public long getUid() {
		return uid;
	}

	@Override
	public OpenPlatform getPlatform() {
		return OpenPlatform.SINA_WEIBO;
	}

	@Override
	public String getUniqueTag() {
		return accessToken;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String info) {
		this.info = info;
	}

}
