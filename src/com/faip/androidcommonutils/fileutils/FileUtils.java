package com.faip.androidcommonutils.fileutils;

import android.os.Environment;

public class FileUtils {

	private FileUtils() {
	}

	/**
	 * If externalStorage writable.
	 */
	public static boolean isExternalStorageMounted() {
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED);

		return !(!canRead || onlyRead || unMounted);
	}

}
