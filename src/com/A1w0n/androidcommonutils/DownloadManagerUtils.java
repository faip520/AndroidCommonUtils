package com.A1w0n.androidcommonutils;

import java.io.File;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

public class DownloadManagerUtils {

	private DownloadManagerUtils() {
	}

	public static long downloadFileWithNotification(final Context context, String url, File tarFile, String title, String description) {
		if (context == null || TextUtils.isEmpty(url) || tarFile == null) {
			return 0;
		}
		
		DownloadManager mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		
		Uri uri = Uri.parse(url);
		
		// If the parent directory doesn't exist, download will fail.
		File parent = tarFile.getParentFile();
		if (parent != null && parent.isDirectory() && !parent.exists()) {
			parent.mkdirs();
		}
	
		long enqueue = mDownloadManager.enqueue(new DownloadManager.Request(uri)
		            .setDestinationUri(Uri.fromFile(tarFile))
		            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
		            .setAllowedOverRoaming(false)
		            .setTitle(title)
		            .setDescription(description)
		            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED));
		
		return enqueue;
	}

}
