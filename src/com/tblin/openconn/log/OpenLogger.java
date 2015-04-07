package com.tblin.openconn.log;

public interface OpenLogger {
	void i(String tag, String msg);

	void d(String tag, String msg);

	void e(String tag, String msg);

	void w(String tag, String msg);
}
