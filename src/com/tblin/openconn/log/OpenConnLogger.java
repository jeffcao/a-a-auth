package com.tblin.openconn.log;

public class OpenConnLogger {
	private static OpenLogger logger;

	/**
	 * 设置一个logger用来打印日志
	 * 
	 * @param logger
	 */
	public static void setLogger(OpenLogger logger) {
		OpenConnLogger.logger = logger;
		i("com.tblin.ad.AdLogger", "logger init");
	}

	public static void i(String msg) {
		i("default_tag", msg);
	}

	public static void i(String tag, String msg) {
		if (logger != null)
			logger.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (logger != null)
			logger.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (logger != null)
			logger.e(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (logger != null)
			logger.w(tag, msg);
	}
}
