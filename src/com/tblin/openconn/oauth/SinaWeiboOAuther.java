package com.tblin.openconn.oauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tblin.openconn.OpenPlatform;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

class SinaWeiboOAuther implements OAuther {

	private String consumerKey;
	private String redirectUrl;
	private Activity activity;
	private Weibo mWeibo;
	private Oauth2AccessToken mToken;

	@Override
	public void oauth() {
		mWeibo = Weibo.getInstance(consumerKey, redirectUrl);
		mWeibo.authorize(activity, new MyWeiboAuth());
	}

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
		this.redirectUrl = param[1];
	}

	private boolean isParamErr(String... param) {
		return param == null || param.length != 2 || param[0] == null
				|| param[1] == null;
	}

	private class MyWeiboAuth implements WeiboAuthListener {

		private Intent broadcast;

		public MyWeiboAuth() {
			broadcast = new Intent();
			broadcast.setAction(OAUTH_FINISH_BROADCAST);
			broadcast.putExtra("type", OpenPlatform.SINA_WEIBO);
		}

		private void sendBroadcast() {
			activity.sendOrderedBroadcast(broadcast, null);
		}

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			mToken = new Oauth2AccessToken(token, expires_in);
			if (mToken.isSessionValid()) {
				/*String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(mToken.getExpiresTime()));*/
				broadcast.putExtra("flag", OAuthFlag.FLAG_OK);
				broadcast.putExtra("token", mToken.getToken());
				broadcast.putExtra("expires", expires_in);
				broadcast.putExtra("uid", values.getString("uid"));
				sendBroadcast();
			} else {
				sendErrBroadcast(OAuthFlag.FLAG_SWB_INVALID_TOKEN);
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			sendErrBroadcast(OAuthFlag.FLAG_SWB_WBEXCEPTION);
		}

		@Override
		public void onError(WeiboDialogError e) {
			sendErrBroadcast(OAuthFlag.FLAG_SWB_WEB_DLG_ERR);
		}

		@Override
		public void onCancel() {
			sendErrBroadcast(OAuthFlag.FLAG_CANCEL);
		}

		private void sendErrBroadcast(int flag) {
			broadcast.putExtra("flag", flag);
			sendBroadcast();
		}

	}

}
