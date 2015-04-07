package com.tblin.openconn.oauth;

import com.tblin.openconn.OpenPlatform;

public interface OAuthRecord {

	public OpenPlatform getPlatform();

	public String getUniqueTag();
	
	public String getInfo();
	
	public void setInfo(String info);

}
