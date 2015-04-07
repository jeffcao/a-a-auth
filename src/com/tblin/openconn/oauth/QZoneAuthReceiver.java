package com.tblin.openconn.oauth;

import com.tblin.openconn.OpenPlatform;
import com.tencent.tauth.TAuthView;
import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.bean.OpenId;
import com.tencent.tauth.http.Callback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class QZoneAuthReceiver extends BroadcastReceiver {

	private Intent broadcast;

	public QZoneAuthReceiver() {
		broadcast = new Intent();
		broadcast.setAction(OAuther.OAUTH_FINISH_BROADCAST);
		broadcast.putExtra("type", OpenPlatform.QZONE);
		broadcast.putExtra("flag", OAuthFlag.FLAG_QZONE_FAIL);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle exts = intent.getExtras();
		// String raw = exts.getString("raw");
		// String expires_in = exts.getString(TAuthView.EXPIRES_IN);
		// String error_des = exts.getString(TAuthView.ERROR_DES);
		String access_token = exts.getString(TAuthView.ACCESS_TOKEN);
		String error_ret = exts.getString(TAuthView.ERROR_RET);
		if (access_token != null && error_ret == null) {
			broadcast.putExtra("access_token", access_token);
			TencentOpenAPI.openid(access_token, new OpenIdCallback(context));
		} else {
			sendBroadcast(context);
		}
	}

	private class OpenIdCallback implements Callback {

		private Context context;

		public OpenIdCallback(Context context) {
			this.context = context;
		}

		@Override
		public void onFail(int ret, final String msg) {
			sendBroadcast(context);
		}

		@Override
		public void onSuccess(Object obj) {
			OpenId openId = (OpenId) obj;
			broadcast.putExtra("flag", OAuthFlag.FLAG_OK);
			broadcast.putExtra("open_id", openId.getOpenId());
			broadcast.putExtra("client_id", openId.getClientId());
			sendBroadcast(context);
		}
	}

	private void sendBroadcast(Context context) {
		context.sendOrderedBroadcast(broadcast, null);
	}

}
