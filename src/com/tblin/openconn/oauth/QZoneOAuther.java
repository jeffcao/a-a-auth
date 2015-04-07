package com.tblin.openconn.oauth;

import android.app.Activity;
import android.content.Intent;

import com.tencent.tauth.TAuthView;

public class QZoneOAuther implements OAuther{
	
	private String clientId;
	private String scope = "get_user_info,get_user_profile,add_share,add_topic,list_album,upload_pic,add_album";
	private String target = "_slef";
	private Activity activity;

	@Override
	public void setParam(Activity activity, String... param) {
		if (activity == null) {
			throw new IllegalArgumentException("must pass an activity");
		}
		if (isParamErr(param)) {
			throw new IllegalArgumentException("must pass correct oauth params");
		}
		this.activity = activity;
		this.clientId = param[0];
		if (param.length == 2)
			this.scope = param[1];
	}

	@Override
	public void oauth() {
		Intent intent = new Intent(activity, com.tencent.tauth.TAuthView.class);
		intent.putExtra(TAuthView.CLIENT_ID, clientId);
		intent.putExtra(TAuthView.SCOPE, scope);
		intent.putExtra(TAuthView.TARGET, target);
		activity.startActivity(intent);
	}
	
	private boolean isParamErr(String... param) {
		return param == null || param.length < 1 || param[0] == null;
	}

}
