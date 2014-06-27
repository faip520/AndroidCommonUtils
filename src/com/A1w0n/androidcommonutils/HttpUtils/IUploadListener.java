package com.A1w0n.androidcommonutils.HttpUtils;

public interface IUploadListener {

	public void transferred(long data);

	public void waitServerResponse();

	public void completed();

}
