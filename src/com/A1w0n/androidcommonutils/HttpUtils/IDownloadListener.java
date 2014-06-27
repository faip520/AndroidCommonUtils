package com.A1w0n.androidcommonutils.HttpUtils;

public interface IDownloadListener {

	public void pushProgress(int progress, int max);

	public void completed();

	public void cancel();
	
	public void error();
}
