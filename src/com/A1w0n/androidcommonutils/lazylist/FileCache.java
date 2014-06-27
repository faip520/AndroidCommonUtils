package com.A1w0n.androidcommonutils.lazylist;

import java.io.File;

import com.A1w0n.androidcommonutils.FileUtils.AndroidFileUtils;

import android.content.Context;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (AndroidFileUtils.isExternalStorageWritable())
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "A1w0nFileCache");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;
	}

	public void clear() {
		if (cacheDir != null && cacheDir.exists()) {
			File[] files = cacheDir.listFiles();
			if (files == null || files.length == 0)
				return;
			for (File f : files)
				f.delete();
		}
	}

}