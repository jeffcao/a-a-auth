package com.tblin.openconn.api;

public interface ApiExecuteCallback {
	
	public static final int FAIL_TWB_API_EXCEPTION = 1;
	public static final int FAIL_QZ_API_EXCEPTION = 2;
	public static final int FAIL_SWB_IOEXCEPTION = 3;
	public static final int FAIL_SWB_WB_EXCEPTION = 4;

	public void onSucess(String result, int resultCode);

	public void onFail(int failCode);

}
