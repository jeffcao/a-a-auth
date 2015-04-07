package com.tblin.openconn.oauth;


import android.app.Activity;
import android.content.Intent;

import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

class TencentWeiboOAuther implements OAuther{
	
	private String consumerKey;
	private String redirectUrl;
	private String consumerSecret;
	private Activity activity;
	private OAuthV2 oAuth;

	@Override
	public void setParam(Activity activity, String... param) {
		if (activity == null) {
			throw new IllegalArgumentException("must pass an activity");
		}
		if (isParamErr(param)) {
			throw new IllegalArgumentException("must pass correct oauth params");
		}
		this.activity = activity;
		this.consumerKey = param[0];
		this.consumerSecret = param[1];
		this.redirectUrl = param[2];
	}

	@Override
	public void oauth() {
		oAuth=new OAuthV2(redirectUrl);
        oAuth.setClientId(consumerKey);
        oAuth.setClientSecret(consumerSecret);
        Intent intent = new Intent(activity, OAuthV2AuthorizeWebView.class);//创建Intent，使用WebView让用户授权
        intent.putExtra("oauth", oAuth);
        activity.startActivityForResult(intent,1);
	}
	
	private boolean isParamErr(String... param) {
		return param == null || param.length != 3 || param[0] == null
				|| param[1] == null || param[2] == null;
	}

}
