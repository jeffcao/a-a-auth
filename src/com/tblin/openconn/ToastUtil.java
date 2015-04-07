package com.tblin.openconn;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private static Toast mToast;

	public static void toast(Context context, String msg) {
		if (msg == null)
			return;
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
		}
		mToast.show();
	}
	
	public static void toast(Context context, int stringId) {
		toast(context, context.getString(stringId));
	}

}
