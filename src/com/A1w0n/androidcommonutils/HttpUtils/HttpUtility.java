package com.A1w0n.androidcommonutils.HttpUtils;

import java.io.File;
import java.util.Map;

/**
 * 所有Http请求的封装
 */
public class HttpUtility {

	private static HttpUtility httpUtility = new HttpUtility();

	private HttpUtility() {
	}

	public static HttpUtility getInstance() {
		return httpUtility;
	}

	public String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param) throws NetworkException {
		return new JavaHttpUtility().executeNormalTask(httpMethod, url, param);
	}

	/**
	 * Http download request.
	 * @param url
	 * @param path
	 * @param downloadListener
	 * @return
	 */
	public boolean executeDownloadTask(String url, File targetFile, IDownloadListener downloadListener) {
		return !Thread.currentThread().isInterrupted() && new JavaHttpUtility().doGetSaveFile(url, targetFile, downloadListener);
	}

	public boolean executeUploadTask(String url, Map<String, String> param, String path, String imageParamName, IUploadListener listener)
			throws NetworkException {
		return !Thread.currentThread().isInterrupted() && new JavaHttpUtility().doUploadFile(url, param, path, imageParamName, listener);
	}
}
