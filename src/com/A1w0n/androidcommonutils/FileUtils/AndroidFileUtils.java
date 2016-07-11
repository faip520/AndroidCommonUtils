package com.A1w0n.androidcommonutils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.A1w0n.androidcommonutils.DebugUtils.Logger;

public class AndroidFileUtils {

	private static final String ERROR_EXTERNAL_STORAGE_NOT_WRITABLE = "External storage not writable!";

	private AndroidFileUtils() {
	}

	// ************************************************InternalStorage****************************************
	/**
	 * 在手机内部存储的/data/data/包名/目录下新建一个文件夹
	 */
	public static File createOrGetDirectoryInInternalStorage(Context context, String directoryName) {
		if (TextUtils.isEmpty(directoryName) || context == null)
			return null;
		return context.getDir(directoryName, Context.MODE_PRIVATE);
	}

	/**
	 * 在手机内部存储的/data/data/包名/目录下删除一个文件夹
	 */
	public static void deleteDirectoryInInternalStorage(Context context, String directoryName) {
		if (TextUtils.isEmpty(directoryName) || context == null)
			return;
		File temp = createOrGetDirectoryInInternalStorage(context, directoryName);
		if (temp != null && temp.exists()) {
			try {
				JavaFileUtils.deleteDirectory(temp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 在App专属的内部存储的中新建一个文件，有可能返回null
	 */
	public static File getOrCreateFileOnInternalStorage(Context context, String relativePath) {
		File result = null;

		if (context == null || TextUtils.isEmpty(relativePath)) {
			Logger.d("Calling createFileOnInternalStorage with illegal arguments!");
			return result;
		}

		return getOrCreateFile(getFullPathOnInternalStorage(context, relativePath));
	}

	/**
	 * Read a file on internal storage. May return null if exception happens.
	 */
	public static FileInputStream getFileOnInternalStorage(Context context, String name) {
		FileInputStream result = null;

		try {
			result = context.openFileInput(name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 可以用来获取文件或者文件夹的绝对路径
	 * 
	 * @param context
	 * @param relativePath
	 * @return
	 */
	public static String getFullPathOnInternalStorage(Context context, String relativePath) {
		if (context == null || TextUtils.isEmpty(relativePath)) {
			Logger.e("Illegal arguments!");
			return null;
		}

		return context.getFilesDir().getAbsolutePath() + File.separator + relativePath;
	}

	public static File getOrCreateDirectoryOnInternalStorage(Context context, String relativePath) {
		File target = null;

		if (TextUtils.isEmpty(relativePath) || context == null) {
			Logger.e("Illegal arguments!");
			return target;
		}

		return getOrCreateDirectory(getFullPathOnInternalStorage(context, relativePath));
	}

	public static boolean deleteFileOnInternalStorage(Context context, String name) {
		return context.deleteFile(name);
	}

	public static void getInternalStorageFreeSpace() {

	}

	public static File getOrCreateFileOnInternalCacheDir(Context context, String fileName) {
		File target = null;

		if (TextUtils.isEmpty(fileName) || context == null) {
			Logger.e("Illegal arguments!");
			return target;
		}

		String fullPath = context.getCacheDir().getAbsolutePath() + File.separator + fileName;
		return getOrCreateFile(fullPath);

	}

	// ************************************************End Of InternalStorage****************************************

	
	
	
	
	// ************************************************ExternalStorage****************************************
	/**
	 * ExternalStorage是否可写
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * ExternalStorage是否可读
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取外部存储的根目录的绝对路径，如果外部存储不可读，就会返回null.
	 * 
	 * @return
	 */
	public static String getExternalStorageRootAbsolutePath() {
		if (!isExternalStorageReadable())
			return null;
		else {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
	}

	/**
	 * Provide a relative path on external storage, return a full path.
	 */
	public static String getFullPathOnExternalStorage(String relativePath) {
		if (TextUtils.isEmpty(relativePath)) {
			Logger.e("Illegal argument!");
			return null;
		}

		if (!isExternalStorageWritable())
			return null;

		String path = getExternalStorageRootAbsolutePath();
		path = path + File.separator + relativePath;
		return path;
	}

	/**
	 * 在外部存储根目录创建或者获取一个文件夹，如果失败了会返回null 这个API如果不是返回null，则目标文件夹是确定存在的
	 */
	public static File getOrCreateDirectoryOnExternalStorage(String relativeDirectoryName) {
		File target = null;

		if (TextUtils.isEmpty(relativeDirectoryName))
			return target;

		if (!isExternalStorageWritable())
			return target;

		return getOrCreateDirectory(getFullPathOnExternalStorage(relativeDirectoryName));
	}

	/**
	 * Try to delete a directory on external storage, return the result.
	 */
	public static boolean deleteDirectoryOnExternalStorage(String relativePath) {
		if (TextUtils.isEmpty(relativePath))
			return false;

		if (!isExternalStorageWritable())
			return false;

		File tarFile = new File(getFullPathOnExternalStorage(relativePath));
		try {
			JavaFileUtils.deleteDirectory(tarFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Try to create a file on external strorage, return null if any failure happens.
	 */
	public static File getOrCreateFileOnExternalStorageRelative(String relativePath) {
		String fullPath = getFullPathOnExternalStorage(relativePath);

		return getOrCreateFileOnExternalStorageFull(fullPath);
	}

	/**
	 * 传入一个Sdcard上的完整的绝对路径，创建相应的文件，有可能返回null
	 * 
	 * @param fullPath
	 * @return
	 */
	public static File getOrCreateFileOnExternalStorageFull(String fullPath) {
		File result = null;

		if (TextUtils.isEmpty(fullPath)) {
			Logger.e("Empty full path!");
			return result;
		}

		if (!isExternalStorageWritable()) {
			Logger.e(ERROR_EXTERNAL_STORAGE_NOT_WRITABLE);
			return result;
		}

		return getOrCreateFile(fullPath);
	}

	public static boolean deleteFileOnExternalStorage(String relativePath) {
		if (TextUtils.isEmpty(relativePath))
			return false;

		if (!isExternalStorageWritable())
			return false;

		File target = new File(getFullPathOnExternalStorage(relativePath));

		return target.delete();
	}
	// ************************************************End of ExternalStorage****************************************
	
	
	
	

	// ************************************************Base****************************************
	/**
	 * 本类的底层方法，这里不会检测SD卡的挂载问题，SD的挂载问题放在本类的上层解决
	 * 
	 * @param relativePath
	 * @return
	 */
	private static File getOrCreateFile(String fullPath) {
		File result = null;

		if (TextUtils.isEmpty(fullPath))
			return result;

		result = new File(fullPath);

		if (!result.exists()) {
			File parentDir = result.getParentFile();
			if (parentDir != null && !parentDir.exists()) {
				parentDir.mkdirs();
			}

			try {
				result.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				result = null;
			}
		}

		return result;
	}

	/**
	 * 本类的底层方法，这里不会检测SD卡的挂载问题，SD的挂载问题放在本类的上层解决
	 * 
	 * @param fullPath
	 * @return
	 */
	private static File getOrCreateDirectory(String fullPath) {
		File target = null;

		if (TextUtils.isEmpty(fullPath))
			return target;

		target = new File(fullPath);

		if (target.exists()) {
			if (target.isDirectory()) {
				return target;
			} else {
				Logger.e("Target already exist, but is not a directory");
				return null;
			}
		}

		// 如果创建文件夹失败了，就返回null
		if (!target.mkdirs()) {
			target = null;
		}

		return target;
	}

	/**
	 * 为指定文件夹创建.nomedia文件 如果传进来的文件夹不存在，不会为调用者创建这个文件夹
	 * 
	 * @param decksDirectory
	 *            会检查传进来的是否是文件夹
	 */
	public static void createNoMediaFileIfMissing(File directory) {
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			Logger.e("Argument is null or not existing!");
		}

		File mediaFile = new File(directory.getAbsolutePath() + "/.nomedia");
		if (!mediaFile.exists()) {
			try {
				mediaFile.createNewFile();
			} catch (IOException e) {
				Logger.e("Nomedia file could not be created in path " + directory.getAbsolutePath());
			}
		}
	}
	// ************************************************End of Base****************************************
}
