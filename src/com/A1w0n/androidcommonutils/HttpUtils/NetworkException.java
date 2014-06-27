package com.A1w0n.androidcommonutils.HttpUtils;

/**
 * Exception thrown by this app's network components.
 */
public class NetworkException extends Exception {

	public String mErrorMessage;
	public int mErrorCode;
	public Throwable mException;

	public NetworkException() {

	}
	
	public NetworkException(String error) {
		mErrorMessage = error;
	}

	public NetworkException(String error, Throwable e) {
		mErrorMessage = error;
		mException = e;
	}
}
