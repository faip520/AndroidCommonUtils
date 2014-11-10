package com.A1w0n.androidcommonutils.AsyncImageUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

/**
 * Cache directory in flash memory.
 */
public class FileCache {

	private File cacheDir;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "FaipCache");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * Get file in the cache directory by image url.
	 */
	public File getFile(String url) {
		String filename = null;
		try {
			filename = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File f = new File(cacheDir, filename);
		return f;
	}

	/**
	 * This class does't provide a put function. If u call getFile and get a file that doesn't
	 * exist, then u can use it to store a image file downloaded from the web.
	 */
	private File putFile(File f) {
		return null;
	}

	/**
	 * Delete all files in the cache directory.
	 */
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}
}