package com.tblin.openconn.api;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tblin.openconn.log.OpenConnLogger;

public class QZoneApiExecutorV2 implements ApiExecutor {

	public static final int CONNECTION_TIMEOUT = 5000;
	public static final int CON_TIME_OUT_MS = 5000;
	public static final int SO_TIME_OUT_MS = 5000;
	public static final int MAX_CONNECTIONS_PER_HOST = 2;
	public static final int MAX_TOTAL_CONNECTIONS = 2;

	@Override
	public void getUserInfo(final ApiExecuteCallback callback,
			final String... params) {
		if (isParamErr(params))
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {
				getInfo(callback, params);
			}
		}).start();
	}

	private void getInfo(final ApiExecuteCallback callback,
			final String... params) {
		String token = params[2], appid = params[1], openid = params[0];
		String url;
		int errcode = -1;
		String respJson = null;
		try {
			url = "https://graph.qq.com/user/get_simple_userinfo?access_token="
					+ URLEncoder.encode(token, "UTF-8")
					+ "&oauth_consumer_key="
					+ URLEncoder.encode(appid, "UTF-8") + "&openid="
					+ URLEncoder.encode(openid, "UTF-8") + "&format=json";
			HttpGet request = new HttpGet(url);
			HttpClient client = createHttpClient(MAX_CONNECTIONS_PER_HOST,
					MAX_TOTAL_CONNECTIONS, CON_TIME_OUT_MS, SO_TIME_OUT_MS);
			HttpResponse response = client.execute(request);
			errcode = response.getStatusLine().getStatusCode();
			if (errcode == 200) {
				String raw_data = EntityUtils.toString(response.getEntity());
				OpenConnLogger.i("QZoneApiExecutorV2 raw data " + raw_data);
				JSONObject root = new JSONObject(raw_data);
				errcode = root.getInt("ret");
				if (errcode == 0) {
					respJson = raw_data;
				}
			}
		} catch (JSONException jse) {
			errcode = 111500;
		} catch (Exception e) {
			errcode = 111404;
			e.printStackTrace();
		}
		if (errcode == 0 && null != respJson) {
			callback.onSucess(respJson, errcode);
		} else {
			callback.onFail(errcode);
		}
	}

	@Override
	public void updateStatuses(ApiExecuteCallback callback, String... params) {
		// TODO Auto-generated method stub

	}

	private boolean isParamErr(String... strings) {
		return strings == null || strings.length != 3 || strings[0] == null
				|| strings[1] == null || strings[2] == null;
	}

	private HttpClient createHttpClient(int maxConnectionPerHost,
			int maxTotalConnections, int conTimeOutMs, int soTimeOutMs) {

		// Register the "http" & "https" protocol scheme, They are required
		// by the default operator to look up socket factories.
		SchemeRegistry supportedSchemes = new SchemeRegistry();
		supportedSchemes.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		// -----------------------------------SSL
		// Scheme------------------------------------------
		try {
			SSLSocketFactory sslSocketFactory = SSLSocketFactory
					.getSocketFactory();
			sslSocketFactory
					.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			supportedSchemes.register(new Scheme("https",
					new QzoneSSLSocketFactory(), 443));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ----------------------------------SSL Scheme
		// end---------------------------------------

		// Prepare parameters.
		HttpParams httpParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(httpParams,
				maxTotalConnections);
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(
				maxConnectionPerHost);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUseExpectContinue(httpParams, false);

		// ThreadSafeClientConnManager connectionManager = new
		// ThreadSafeClientConnManager(httpParams,supportedSchemes);
		SingleClientConnManager singleClientConnManager = new SingleClientConnManager(
				httpParams, supportedSchemes);
		HttpConnectionParams.setConnectionTimeout(httpParams, conTimeOutMs);
		HttpConnectionParams.setSoTimeout(httpParams, soTimeOutMs);

		HttpClientParams.setCookiePolicy(httpParams,
				CookiePolicy.BROWSER_COMPATIBILITY);
		// httpClient=new DefaultHttpClient(connectionManager, httpParams);
		return new DefaultHttpClient(singleClientConnManager, httpParams);
	}

	public class QzoneSSLSocketFactory implements SocketFactory,
			LayeredSocketFactory {

		private SSLContext sslcontext = null;

		private SSLContext getEasySSLContext() throws IOException {
			try {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, new TrustManager[] { new X509TrustManager() {
					public void checkClientTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
					}

					public void checkServerTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						if (null == chain) {
							throw new CertificateException(ErrorCodeConstants
									.getErrmsg("2001"));
						}
						boolean check = false;
						for (X509Certificate x509Certificate : chain) {
							if (x509Certificate.getSubjectDN().toString()
									.startsWith("CN=graph.qq.com,")) {
								check = true;
								break;
							}
						}
						if (check == false) {
							throw new CertificateException(ErrorCodeConstants
									.getErrmsg("2002"));
						}
					}

					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				} }, null);
				return context;
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
		}

		private SSLContext getSSLContext() throws IOException {
			if (this.sslcontext == null) {
				this.sslcontext = getEasySSLContext();
			}
			return this.sslcontext;
		}

		public Socket connectSocket(Socket sock, String host, int port,
				InetAddress localAddress, int localPort, HttpParams params)
				throws IOException, UnknownHostException,
				ConnectTimeoutException {
			int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
			int soTimeout = HttpConnectionParams.getSoTimeout(params);

			InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
			SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock
					: createSocket());

			if ((localAddress != null) || (localPort > 0)) {
				if (localPort < 0) {
					localPort = 0;
				}
				InetSocketAddress isa = new InetSocketAddress(localAddress,
						localPort);
				sslsock.bind(isa);
			}

			sslsock.connect(remoteAddress, connTimeout);
			sslsock.setSoTimeout(soTimeout);
			return sslsock;

		}

		public Socket createSocket() throws IOException {
			return getSSLContext().getSocketFactory().createSocket();
		}

		public boolean isSecure(Socket socket) throws IllegalArgumentException {
			return true;
		}

		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(socket,
					host, port, autoClose);
		}

		public boolean equals(Object obj) {
			return ((obj != null) && obj.getClass().equals(
					QzoneSSLSocketFactory.class));
		}

		public int hashCode() {
			return QzoneSSLSocketFactory.class.hashCode();
		}

	}

	/**
	 * 本类针对 QzoneSDK， 设定了一系列的 errcode 以及对应的 errmsg
	 */
	public static class ErrorCodeConstants {

		private static MyErrorCodeHashMap myErrorCodeHashMap = new MyErrorCodeHashMap();

		public static String getErrmsg(String errcode) {
			return myErrorCodeHashMap.get(errcode);
		}
	}

	static class MyErrorCodeHashMap extends HashMap<String, String> {
		private static final long serialVersionUID = 2427025312680000207L;

		public MyErrorCodeHashMap() {
			// TODO errcode尚未确定
			put("1", "connect out of time");

			// OAuthClient错误
			put("1001", "qHttpClient not specified");

			// 证书验证错误
			put("2001", "Can not receive the certificates from server.");
			put("2002",
					"The name on the security certificate is invalid or does not match  \"graph.qq.com\".");

		}
	}

}
