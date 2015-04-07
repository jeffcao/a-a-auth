package com.tblin.openconn.oauth;

import android.app.Activity;

import com.tblin.openconn.OpenPlatform;

public class OAutherFactory {

	/**
	 * 获取授权类
	 * 
	 * @param activity
	 * @param type
	 * @param param
	 *            <li>
	 *            新浪微博:consumer key; redirect url; <li>
	 *            腾讯微博: tweibo_consumer_key; tweibo_consumer_secret;
	 *            tweibo_redirect_url;<li>
	 *            QQ登录：client_id; scope(不传则为默认)
	 * @return
	 */
	public static OAuther getOAuther(Activity activity, OpenPlatform type,
			String... param) {
		OAuther oauther = null;
		switch (type) {
		case SINA_WEIBO:
			oauther = new SinaWeiboOAuther();
			break;
		case TENCENT_WEIBO:
			oauther = new TencentWeiboOAuther();
			break;
		case QZONE:
			oauther = new QZoneOAuther();
			break;
		}
		if (oauther != null)
			oauther.setParam(activity, param);
		return oauther;
	}

}
