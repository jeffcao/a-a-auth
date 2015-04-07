package com.tblin.openconn.api;

interface ApiExecutor {

	public void getUserInfo(ApiExecuteCallback callback, String... params);

	public void updateStatuses(ApiExecuteCallback callback, String... params);
}
