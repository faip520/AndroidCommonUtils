package com.faip.androidcommonutils.fileutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.faip.androidcommonutils.ioutils.IOUtils;

import android.content.Context;
import android.os.Environment;

public class FileUtils {

	private FileUtils() {
	}

	/**
	 * If externalStorage mounted.
	 */
	public static boolean isExternalStorageMounted() {
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED);

		return !(!canRead || onlyRead || unMounted);
	}
	
	public static String getExternalStorageFolder() {
		String result = null;
		if (isExternalStorageMounted()) {
			result = Environment.getExternalStorageDirectory().getPath();
		}
		
		return result;
	}
	
    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            FileOutputStream output = openOutputStream(destination);
            try {
                IOUtils.copy(source, output);
                output.close(); // don't swallow close Exception if copy completes normally
            } finally {
                IOUtils.closeSilently(output);
            }
        } finally {
            IOUtils.closeSilently(source);
        }
    }
    
    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }
    
    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

}
