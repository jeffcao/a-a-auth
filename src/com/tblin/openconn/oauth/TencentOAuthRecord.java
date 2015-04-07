package com.tblin.openconn.oauth;

public abstract class TencentOAuthRecord implements OAuthRecord {

	private String clientId;
	private String openId;
	private String accessToken;
	private String info;

	public TencentOAuthRecord(String clientId, String openId, String accessToken) {
		super();
		this.clientId = clientId;
		this.openId = openId;
		this.accessToken = accessToken;
	}

	public String getClientId() {
		return clientId;
	}

	public String getOpenId() {
		return openId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public String getUniqueTag() {
		return openId;
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
