package com.faip.androidcommonutils.httputils;

/**
 * Just like a interface.
 */
public class FileDownloaderHttpHelper {
	public static class DownloadListener {
		public void pushProgress(int progress, int max) {
		}

		public void completed() {
		}

		public void cancel() {
		}
	}
}
