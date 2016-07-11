package com.A1w0n.androidcommonutils.HttpUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.A1w0n.androidcommonutils.BuildConfig;
import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;
import com.A1w0n.androidcommonutils.IOUtils.IOUtils;
import com.A1w0n.androidcommonutils.NetworkUtils.NetworkUtils;
import com.A1w0n.androidcommonutils.R;
import com.A1w0n.androidcommonutils.DebugUtils.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 *
 */
public class JavaHttpUtility {

	private static final int CONNECT_TIMEOUT = 10 * 1000;
	private static final int READ_TIMEOUT = 10 * 1000;

	private static final int DOWNLOAD_CONNECT_TIMEOUT = 15 * 1000;
	private static final int DOWNLOAD_READ_TIMEOUT = 60 * 1000;

	private static final int UPLOAD_CONNECT_TIMEOUT = 15 * 1000;
	private static final int UPLOAD_READ_TIMEOUT = 5 * 60 * 1000;
	
	private static JavaHttpUtility mInstance;
	
	public static JavaHttpUtility getInstance() {
		if (mInstance == null) {
			mInstance = new JavaHttpUtility();
		}
		
		return mInstance;
	}

	// ===================NullHostNameVerifier================
	public class NullHostNameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	// =================================================

	private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}
	} };

	private JavaHttpUtility() {
		// allow Android to use an untrusted certificate for SSL/HTTPS connection
		// so that when you debug app, you can use Fiddler http://fiddler2.com to logs all HTTPS
		// traffic
		try {
			if (BuildConfig.DEBUG) {
				HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			}
		} catch (Exception e) {
		}
	}

	public String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param) throws NetworkException {
		switch (httpMethod) {
		case Post:
			return doPost(url, param);
		case Get:
			return doGet(url, param);
		}
		return "";
	}

	/**
	 * Return a Proxy instance of system's proxy setting.
	 */
	private static Proxy getProxy() {
		String proxyHost = System.getProperty("http.proxyHost");
		String proxyPort = System.getProperty("http.proxyPort");
		if (!TextUtils.isEmpty(proxyHost) && !TextUtils.isEmpty(proxyPort))
			return new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.valueOf(proxyPort)));
		else
			return null;
	}

	/**
	 * Do post request by url with params.
	 * 
	 * @param urlAddress
	 * @param param
	 * @return
	 * @throws NetworkException
	 */
	public String doPost(String urlAddress, Map<String, String> param) throws NetworkException {
		
		if (!NetworkUtils.isNetworkConnected(GlobalApplication.getInstance())) {
			Logger.e("Cannot do a http post  without network connection!");
			return null;
		}
		
		GlobalApplication globalContext = GlobalApplication.getInstance();
		String errorStr = globalContext.getString(R.string.hu_time_out);
		globalContext = null;
		try {
			URL url = new URL(urlAddress);
			Proxy proxy = getProxy();
			HttpURLConnection uRLConnection;
			if (proxy != null)
				uRLConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				uRLConnection = (HttpURLConnection) url.openConnection();

			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);
			uRLConnection.setConnectTimeout(CONNECT_TIMEOUT);
			uRLConnection.setReadTimeout(READ_TIMEOUT);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Connection", "Keep-Alive");
			uRLConnection.setRequestProperty("Charset", "UTF-8");
			uRLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			uRLConnection.connect();

			DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
			out.write(HttpUtils.encodeUrl(param).getBytes());
			out.flush();
			out.close();
			return handleResponse(uRLConnection);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException(errorStr, e);
		}
	}

	/**
	 * 
	 * @param httpURLConnection
	 * @return
	 * @throws NetworkException
	 */
	private String handleResponse(HttpURLConnection httpURLConnection) throws NetworkException {
		GlobalApplication globalContext = GlobalApplication.getInstance();
		String errorStr = globalContext.getString(R.string.hu_time_out);
		globalContext = null;
		int status = 0;
		try {
			status = httpURLConnection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			httpURLConnection.disconnect();
			throw new NetworkException(errorStr, e);
		}

		if (status != HttpURLConnection.HTTP_OK) {
			// Will throw a DVHttpException in most cases.
			return handleError(httpURLConnection);
		}

		return readResult(httpURLConnection);
	}

	/**
	 * 
	 * @param urlConnection
	 * @return
	 * @throws NetworkException
	 */
	private String handleError(HttpURLConnection urlConnection) throws NetworkException {
		// Get error message. In most cases, a DVHttpException will be thrown within the call of
		// readError()
		String result = readError(urlConnection);
		String err = null;
		int errCode = 0;
		try {
			Logger.e("error=" + result);
			JSONObject json = new JSONObject(result);
			err = json.optString("error_description", "");
			if (TextUtils.isEmpty(err))
				err = json.getString("error");
			errCode = json.getInt("error_code");
			NetworkException exception = new NetworkException();
			exception.mErrorCode = errCode;
			exception.mErrorMessage = err;
			throw exception;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Get result string of the HttpURLConection.
	 * 
	 * @param urlConnection
	 * @return
	 * @throws NetworkException
	 */
	private String readResult(HttpURLConnection urlConnection) throws NetworkException {
		InputStream is = null;
		BufferedReader buffer = null;
		GlobalApplication globalContext = GlobalApplication.getInstance();
		String errorStr = globalContext.getString(R.string.hu_time_out);
		globalContext = null;
		try {
			is = urlConnection.getInputStream();

			String content_encode = urlConnection.getContentEncoding();

			if (null != content_encode && !"".equals(content_encode) && content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			buffer = new BufferedReader(new InputStreamReader(is));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			Logger.d("result=" + strBuilder.toString());
			return strBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException(errorStr, e);
		} finally {
			IOUtils.closeSilently(is);
			IOUtils.closeSilently(buffer);
			urlConnection.disconnect();
		}
	}

	/**
	 * Return error message. This is called only when some network connection problems happen.
	 * However I think this will not happen very often.
	 * 
	 * @param urlConnection
	 * @return
	 * @throws NetworkException
	 */
	private String readError(HttpURLConnection urlConnection) throws NetworkException {
		InputStream is = null;
		BufferedReader buffer = null;
		GlobalApplication globalContext = GlobalApplication.getInstance();
		String errorStr = globalContext.getString(R.string.hu_time_out);

		try {
			is = urlConnection.getErrorStream();
			// getErrorStream always return null in our cases.
			if (is == null) {
				errorStr = globalContext.getString(R.string.hu_unknown_network_error);
				throw new NetworkException(errorStr);
			}

			String content_encode = urlConnection.getContentEncoding();

			if (null != content_encode && !"".equals(content_encode) && content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			buffer = new BufferedReader(new InputStreamReader(is));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			Logger.d("error result=" + strBuilder.toString());
			return strBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException(errorStr, e);
		} finally {
			IOUtils.closeSilently(is);
			IOUtils.closeSilently(buffer);
			urlConnection.disconnect();
			globalContext = null;
		}
	}

	/**
	 * 
	 * @param urlStr
	 * @param param
	 * @return
	 * @throws NetworkException
	 */
	public String doGet(String urlStr, Map<String, String> param) throws NetworkException {
		
		if (!NetworkUtils.isNetworkConnected(GlobalApplication.getInstance())) {
			Logger.e("Cannot do a http get  without network connection!");
			return null;
		}
		
		GlobalApplication globalContext = GlobalApplication.getInstance();
		String errorStr = globalContext.getString(R.string.hu_time_out);
		globalContext = null;
		InputStream is = null;
		try {

			StringBuilder urlBuilder = new StringBuilder(urlStr);
			urlBuilder.append("?").append(HttpUtils.encodeUrl(param));
			URL url = new URL(urlBuilder.toString());
			Logger.d("get request" + url);
			Proxy proxy = getProxy();
			HttpURLConnection urlConnection;
			if (proxy != null)
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(false);
			urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
			urlConnection.setReadTimeout(READ_TIMEOUT);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			urlConnection.connect();

			return handleResponse(urlConnection);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException(errorStr, e);
		}
	}

	/**
	 * @param urlStr
	 * @param targetFile
	 * @param downloadListener
	 * @return
	 */
	public boolean doGetSaveFile(String urlStr, File targetFile, IDownloadListener downloadListener) {
		if (TextUtils.isEmpty(urlStr)) {
			Logger.e("Empty url!");
			if (downloadListener != null) {
				downloadListener.error();
			}
			return false;
		}
		
		if (targetFile == null || !targetFile.canWrite()) {
			Logger.e("Target file not writable!");
			if (downloadListener != null) {
				downloadListener.error();
			}
			return false;
		}
		
		if (!NetworkUtils.isNetworkConnected(GlobalApplication.getInstance())) {
			Logger.e("Cannot download file without network connection!");
			if (downloadListener != null) {
				downloadListener.error();
			}
			return false;
		}

		BufferedOutputStream out = null;
		InputStream in = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlStr);
			Logger.d("download request=" + urlStr);
			Proxy proxy = getProxy();
			if (proxy != null)
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(false);
			urlConnection.setConnectTimeout(DOWNLOAD_CONNECT_TIMEOUT);
			urlConnection.setReadTimeout(DOWNLOAD_READ_TIMEOUT);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			urlConnection.connect();

			int status = urlConnection.getResponseCode();

			if (status != HttpURLConnection.HTTP_OK) {
				if (downloadListener != null) {
					downloadListener.error();
				}
				return false;
			}

			int bytetotal = (int) urlConnection.getContentLength();
			Logger.d("想要下载的文件大小是：" + bytetotal);
			if (bytetotal > 0 && targetFile.exists() && targetFile.length() == bytetotal) {
				Logger.d("文件已经存在，并且大小一致，下载已取消！");
				if (downloadListener != null) {
					downloadListener.cancel();
				}
				return true;
			}
			
			int bytesum = 0;
			int byteread = 0;
			out = new BufferedOutputStream(new FileOutputStream(targetFile));
			in = new BufferedInputStream(urlConnection.getInputStream());

			final Thread thread = Thread.currentThread();
			byte[] buffer = new byte[1444];
			while ((byteread = in.read(buffer)) != -1) {
				if (thread.isInterrupted()) {
					targetFile.delete();
					IOUtils.closeSilently(out);
					throw new InterruptedIOException();
				}

				bytesum += byteread;
				out.write(buffer, 0, byteread);
				if (downloadListener != null && bytetotal > 0) {
					downloadListener.pushProgress(bytesum, bytetotal);
				}
			}
			if (downloadListener != null) {
				downloadListener.completed();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			if (downloadListener != null) {
				downloadListener.error();
			}
		} finally {
			IOUtils.closeSilently(in);
			IOUtils.closeSilently(out);
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return false;
	}
	
	public boolean doGetSaveFile(String urlStr, File targetFile) {
		if (TextUtils.isEmpty(urlStr)) {
			Logger.e("Empty url!");
			return false;
		}
		
		if (targetFile == null || !targetFile.canWrite()) {
			Logger.e("Target file not writable!");
			return false;
		}
		
		if (!NetworkUtils.isNetworkConnected(GlobalApplication.getInstance())) {
			Logger.e("Cannot download file without network connection!");
			return false;
		}

		BufferedOutputStream out = null;
		InputStream in = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlStr);
			Proxy proxy = getProxy();
			if (proxy != null)
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(false);
			urlConnection.setConnectTimeout(DOWNLOAD_CONNECT_TIMEOUT);
			urlConnection.setReadTimeout(DOWNLOAD_READ_TIMEOUT);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			urlConnection.connect();

			int status = urlConnection.getResponseCode();

			if (status != HttpURLConnection.HTTP_OK) {
				return false;
			}

			int bytetotal = (int) urlConnection.getContentLength();
			if (bytetotal > 0 && targetFile.exists() && targetFile.length() == bytetotal) {
				Logger.d("Same file with the same size already exits, download canceled!");
				return true;
			}
			
			int bytesum = 0;
			int byteread = 0;
			out = new BufferedOutputStream(new FileOutputStream(targetFile));
			in = new BufferedInputStream(urlConnection.getInputStream());

			final Thread thread = Thread.currentThread();
			byte[] buffer = new byte[1444];
			while ((byteread = in.read(buffer)) != -1) {
				if (thread.isInterrupted()) {
					targetFile.delete();
					IOUtils.closeSilently(out);
					throw new InterruptedIOException();
				}

				bytesum += byteread;
				out.write(buffer, 0, byteread);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			IOUtils.closeSilently(in);
			IOUtils.closeSilently(out);
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	private static String getBoundry() {
		StringBuffer _sb = new StringBuffer();
		for (int t = 1; t < 12; t++) {
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0) {
				_sb.append((char) time % 9);
			} else if (time % 3 == 1) {
				_sb.append((char) (65 + time % 26));
			} else {
				_sb.append((char) (97 + time % 26));
			}
		}
		return _sb.toString();
	}

	private String getBoundaryMessage(String boundary, Map params, String fileField, String fileName, String fileType) {
		StringBuffer res = new StringBuffer("--").append(boundary).append("\r\n");

		Iterator keys = params.keySet().iterator();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			String value = (String) params.get(key);
			res.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n").append("\r\n").append(value).append("\r\n").append("--")
					.append(boundary).append("\r\n");
		}
		res.append("Content-Disposition: form-data; name=\"").append(fileField).append("\"; filename=\"").append(fileName).append("\"\r\n")
				.append("Content-Type: ").append(fileType).append("\r\n\r\n");

		return res.toString();
	}

	/**
	 * 已经测试过可以正常使用的
     *
	 * @param urlStr
	 * @param param
	 * @param path
	 * @param imageParamName
	 * @param listener
	 * @return
	 * @throws NetworkException
	 */
	public boolean doUploadFile(String urlStr, Map<String, String> param, String path, String imageParamName,
			final IUploadListener listener) throws NetworkException {
		String BOUNDARYSTR = getBoundry();

		File targetFile = new File(path);

		byte[] barry = null;
		int contentLength = 0;
		String sendStr = "";
		try {
			barry = ("--" + BOUNDARYSTR + "--\r\n").getBytes("UTF-8");

			sendStr = getBoundaryMessage(BOUNDARYSTR, param, imageParamName, new File(path).getName(), "image/png");
			contentLength = sendStr.getBytes("UTF-8").length + (int) targetFile.length() + 2 * barry.length;
		} catch (UnsupportedEncodingException e) {

		}
		int totalSent = 0;
		String lenstr = Integer.toString(contentLength);

		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		FileInputStream fis = null;
		GlobalApplication globalContext = GlobalApplication.getInstance();
		globalContext = null;
		try {
			URL url = null;

			url = new URL(urlStr);

			Proxy proxy = getProxy();
			if (proxy != null)
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setConnectTimeout(UPLOAD_CONNECT_TIMEOUT);
			urlConnection.setReadTimeout(UPLOAD_READ_TIMEOUT);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
			urlConnection.setRequestProperty("Content-Length", lenstr);
			((HttpURLConnection) urlConnection).setFixedLengthStreamingMode(contentLength);
			urlConnection.connect();

			out = new BufferedOutputStream(urlConnection.getOutputStream());
			out.write(sendStr.getBytes("UTF-8"));
			totalSent += sendStr.getBytes("UTF-8").length;

			fis = new FileInputStream(targetFile);

			int bytesRead;
			int bytesAvailable;
			int bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024;

			bytesAvailable = fis.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			bytesRead = fis.read(buffer, 0, bufferSize);
			long transferred = 0;
			final Thread thread = Thread.currentThread();
			while (bytesRead > 0) {

				if (thread.isInterrupted()) {
					targetFile.delete();
					throw new InterruptedIOException();
				}
				out.write(buffer, 0, bufferSize);
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
				transferred += bytesRead;
				if (transferred % 50 == 0)
					out.flush();
				if (listener != null)
					listener.transferred(transferred);

			}

			out.write(barry);
			totalSent += barry.length;
			out.write(barry);
			totalSent += barry.length;
			out.flush();
			out.close();
			if (listener != null) {
				listener.waitServerResponse();
			}
			int status = urlConnection.getResponseCode();

			if (status != HttpURLConnection.HTTP_OK) {
				String error = handleError(urlConnection);
				throw new NetworkException(error);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException("", e);
		} finally {
			IOUtils.closeSilently(fis);
			IOUtils.closeSilently(out);
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return true;
	}

    /**
     * Get ip address by host, and cache it in SharePreferences.
     * You can call this as soon as you enter a activity, by which,
     * you can speed up your http request fired latter.
     *
     * @param context
     * @param host
     */
    public static void lookupIP(Context context, String host) {
        SharedPreferences preferences = context.getSharedPreferences("host", 0);

        String ip;

        try {
            // 通过DNS查询对应的host的ip地址
            InetAddress address = InetAddress.getByName(host);
            ip = address.getHostAddress();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(host, ip);
            editor.commit();
        } catch (UnknownHostException e) {
        }
    }

}
