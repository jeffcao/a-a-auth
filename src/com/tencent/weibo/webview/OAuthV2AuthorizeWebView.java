package com.tencent.weibo.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.tblin.openconn.OpenPlatform;
import com.tblin.openconn.oauth.OAuthFlag;
import com.tblin.openconn.oauth.OAuther;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;

public class OAuthV2AuthorizeWebView extends Activity {

	private Intent intent;
	private OAuthV2 oAuth;
	private ProgressDialog mSpinner;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout linearLayout = new LinearLayout(this);
		WebView webView = new WebView(this);
		linearLayout.addView(webView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		setContentView(linearLayout);
		initBroadcast();
		oAuth = (OAuthV2) getIntent().getExtras().getSerializable("oauth");
		startWebView(webView);
		mSpinner = new ProgressDialog(this);
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("请求中，请稍侯...");
	}

	@Override
	public void finish() {
		sendBroadcast();
		super.finish();
	}

	@Override
	protected void onDestroy() {
		sendBroadcast();
		super.onDestroy();
	}

	public void initBroadcast() {
		sendFlag = false;
		intent = new Intent();
		intent.setAction(OAuther.OAUTH_FINISH_BROADCAST);
		intent.putExtra("type", OpenPlatform.TENCENT_WEIBO);
		intent.putExtra("flag", OAuthFlag.FLAG_TWEIBO_FAIL);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void startWebView(WebView webView) {
		String urlStr = OAuthV2Client.generateImplicitGrantUrl(oAuth);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webView.requestFocus();
		webView.loadUrl(urlStr);
		WebViewClient client = new WebViewClient() {
			/**
			 * 回调方法，当页面开始加载时执行 在授权成功后，最后会有一次回调，回调的url中附带有授权信息和应用中的回调地址，形如： URL =
			 * http://www.tblin.com/
			 * #access_token=d29d0d758bc6d0c72f0c702b0f234cef
			 * &expires_in=604800&openid=7318B51A2920BF8932C6288906A8CF6A
			 * &openkey=1F51E05B37977768B26820296EF8001F
			 * &refresh_token=807a8f18e9ecedea6d36a478ea8c4ae2 &name=qinyuanz
			 * &nick=true_story
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url.indexOf("access_token=") != -1) {
					int start = url.indexOf("access_token=");
					String responseData = url.substring(start);
					OAuthV2Client
							.parseAccessTokenAndOpenId(responseData, oAuth);
					intent.putExtra("flag", OAuthFlag.FLAG_OK);
					intent.putExtra("open_id", oAuth.getOpenid());
					intent.putExtra("client_id", oAuth.getClientId());
					intent.putExtra("access_token", oAuth.getAccessToken());
					view.destroyDrawingCache();
					view.destroy();
					finish();
					return;
				}
				mSpinner.show();
				super.onPageStarted(view, url, favicon);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				mSpinner.dismiss();
				super.onPageFinished(view, url);
			}

		};
		webView.setWebViewClient(client);
	}

	private boolean sendFlag = false;

	private void sendBroadcast() {
		if (!sendFlag) {
			sendOrderedBroadcast(intent, null);
			sendFlag = true;
		}
	}

}
