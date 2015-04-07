package com.tblin.openconn;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.example.openinteconnect.R;
import com.tblin.openconn.oauth.OAuther;
import com.tblin.openconn.oauth.QZoneAuthReceiver;
import com.tencent.tauth.TAuthView;

/**
 * 启动这个Activity要在Intent中传入相关的授权信息 <li>
 * 新浪微博：sweibo_consumer_key, sweibo_redirect_url <li>
 * 腾讯微博: tweibo_consumer_key, tweibo_consumer_secret, tweibo_redirect_url <li>
 * QQ登录：client_id, scope(不传则为默认)
 * 
 * @author qy
 * 
 */
public class OAuthActivity extends Activity {

	public static final String SWEIBO_CONSUMER_KEY = "sweibo_consumer_key";
	public static final String SWEIBO_REDIRECT_URL = "sweibo_redirect_url";
	public static final String TWEIBO_CONSUMER_KEY = "tweibo_consumer_key";
	public static final String TWEIBO_CONSUMER_SECRET = "tweibo_consumer_secret";
	public static final String TWEIBO_REDIRECT_URL = "tweibo_redirect_url";
	public static final String QQ_CLIENT_ID = "qq_client_id";
	public static final String QQ_SCOPE = "qq_scope";
	public static final String ACTION_OAUTH_COMPLETE = OAuther.OAUTH_FINISH_BROADCAST;
	private BroadcastReceiver oauthReceiver;
	private QZoneAuthReceiver qzoneAuthReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oauth);
		initBtnClick();
		registReceiver();
	}

	@Override
	protected void onDestroy() {
		unregistReceiver();
		super.onDestroy();
	}

	private void initBtnClick() {
		View sinaWeibo = findViewById(R.id.activity_oauth_sina_weibo);
		View tencentWeibo = findViewById(R.id.activity_oauth_tencent_weibo);
		View qq = findViewById(R.id.activity_oauth_qq);
		sinaWeibo.setOnClickListener(new OAuthBtnClickListener(this, OpenPlatform.SINA_WEIBO));
		tencentWeibo.setOnClickListener(new OAuthBtnClickListener(this, OpenPlatform.TENCENT_WEIBO));
		qq.setOnClickListener(new OAuthBtnClickListener(this, OpenPlatform.QZONE));
	}

	private void registReceiver() {
		oauthReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		IntentFilter filter = new IntentFilter(OAuther.OAUTH_FINISH_BROADCAST);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(oauthReceiver, filter);
		qzoneAuthReceiver = new QZoneAuthReceiver();
		registerReceiver(qzoneAuthReceiver, new IntentFilter(
				TAuthView.AUTH_BROADCAST));
	}

	private void unregistReceiver() {
		if (oauthReceiver != null) {
			unregisterReceiver(oauthReceiver);
		}
		if (qzoneAuthReceiver != null) {
			unregisterReceiver(qzoneAuthReceiver);
		}
	}

}
