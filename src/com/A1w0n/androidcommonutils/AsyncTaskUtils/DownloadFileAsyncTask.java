package com.A1w0n.androidcommonutils.AsyncTaskUtils;

import com.A1w0n.androidcommonutils.HttpUtils.IDownloadListener;


public class DownloadFileAsyncTask extends BaseAsyncTask<String, Void, Boolean> {
	
	private String mUrl;
	private String mPath;
	private IDownloadListener mDownloadListener;
	
	// 测试URL
	String url = "http://www.xiami.com/software/download?spm=a1z1s.3522017.23310317.6.eIYwJ7&app=music_android";

	public DownloadFileAsyncTask(String url, String pathOnExternal, IDownloadListener listener) {
		mUrl = url;
		mPath = pathOnExternal;
		mDownloadListener = listener;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		//return HttpUtility.getInstance().executeDownloadTask(mUrl, mPath, mDownloadListener);
		return null;
	}
}
