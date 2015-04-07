package com.tblin.openconn.oauth;

import android.app.Activity;

public interface OAuther {

	/**
	 * 授权完成的时候，发送这个广播
	 */
	public static final String OAUTH_FINISH_BROADCAST = "oauth_finish";

	public void setParam(Activity activity, String... param);

	public void oauth();
}
