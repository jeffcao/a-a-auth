package com.tblin.openconn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.example.openinteconnect.R;
import com.tblin.openconn.oauth.OAuther;
import com.tblin.openconn.oauth.OAutherFactory;

/**
 * 使用这个类，传入的activity必须把授权相关的数据按照要求放在Intent的Extra数据里面
 * 
 * @author qy
 *
 */
public class OAuthBtnClickListener implements View.OnClickListener {

	private Activity activity;
	private OpenPlatform platform;

	public OAuthBtnClickListener(Activity activity, OpenPlatform platform) {
		this.activity = activity;
		this.platform = platform;
	}

	@Override
	public void onClick(View v) {
		if (!isNetConnected()) {
			ToastUtil.toast(activity, R.string.notify_no_net);
			return;
		}
		String[] param = null;
		switch (platform) {
		case SINA_WEIBO:
			param = genSinaWeiboParam();
			break;
		case TENCENT_WEIBO:
			param = genTencentWeiboParam();
			break;
		case QZONE:
			param = genQzoneParam();
			break;
		}
		OAuther oauther = OAutherFactory.getOAuther(activity, platform, param);
		if (oauther != null) {
			oauther.oauth();
		}
	}

	private String[] genTencentWeiboParam() {
		Intent it = activity.getIntent();
		String[] param = new String[3];
		param[0] = it.getStringExtra(OAuthActivity.TWEIBO_CONSUMER_KEY);
		param[1] = it.getStringExtra(OAuthActivity.TWEIBO_CONSUMER_SECRET);
		param[2] = it.getStringExtra(OAuthActivity.TWEIBO_REDIRECT_URL);
		return param;
	}

	private String[] genSinaWeiboParam() {
		Intent it = activity.getIntent();
		String[] param = new String[2];
		param[0] = it.getStringExtra(OAuthActivity.SWEIBO_CONSUMER_KEY);
		param[1] = it.getStringExtra(OAuthActivity.SWEIBO_REDIRECT_URL);
		return param;
	}

	private String[] genQzoneParam() {
		Intent it = activity.getIntent();
		String[] param = null;
		String scope = it.getStringExtra(OAuthActivity.QQ_SCOPE);
		if (scope == null) {
			param = new String[1];
		} else {
			param = new String[0];
			param[1] = scope;
		}
		param[0] = it.getStringExtra(OAuthActivity.QQ_CLIENT_ID);
		return param;
	}
	
	private boolean isNetConnected() {
		ConnectivityManager mgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mgr.getActiveNetworkInfo();
		return info != null && info.isAvailable() && info.isConnected();
	}

}
