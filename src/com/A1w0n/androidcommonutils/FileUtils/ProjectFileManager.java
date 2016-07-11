package com.A1w0n.androidcommonutils.FileUtils;

import java.io.File;

import android.text.TextUtils;

import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;
import com.A1w0n.androidcommonutils.DebugUtils.Logger;

/**
 * @author A1w0n
 * 项目必须确保，项目用到的所有File对象，都是这里出来的！
 * 
 * 会自动为每个需要创建的文件夹创建.nomedia文件
 */
public class ProjectFileManager {
	
	public static final String PATH_A1W0N = "A1w0n";
	public static final String PATH_APK_ICON = PATH_A1W0N + File.separator + "apkIcons";
	public static final String FILENAME_ALL_APPS_INFO = "app.info";
	public static final String PATH_ALL_APPS_INFO_FILE = PATH_A1W0N + File.separator + FILENAME_ALL_APPS_INFO;

    private static final String PATH_LOGGER_FILE = PATH_A1W0N + File.separator + "project.log";
	

	public ProjectFileManager() {
	}

    /**
     * 获取日志文件，为了方便还没root手机的用户获取log日志，
     * 把日志放在sdcard比较好
     *
     * 为了方便定位某些比较难重现的bug，有必要在代码的很多
     * 地方打上输出log到日志文件
     *
     * @return
     */
    public static File getLoggerFile() {
        return getFile(PATH_LOGGER_FILE);
    }

    public static File getIconFileForWriting(String iconFileName) {
		String relativePath = PATH_APK_ICON + File.separator + iconFileName + ".png";
		return getFile(relativePath);
	}
	
	public static File getAllAppsInfoFileForWriting() {
		String relativePath = PATH_ALL_APPS_INFO_FILE;
		return getFile(relativePath);
	}

	private static File getFile(String relativePath) {
		File result = null;
		
		if (TextUtils.isEmpty(relativePath)) {
			Logger.e("Try to access a file with empty relative path!");
			return result;
		}
		
		if (AndroidFileUtils.isExternalStorageWritable()) {
			result = AndroidFileUtils.getOrCreateFileOnExternalStorageRelative(relativePath);
		} else {
			result = AndroidFileUtils.getOrCreateFileOnInternalStorage(GlobalApplication.getInstance(), relativePath);
		}
		
		return result;
	}
	
	/**
	 * 整个项目，只知道相对路径，而不用关系内部存储还是外部存储，要想得到文件夹的话，
	 * 调用这个方法，通过相对路径获取文件夹
	 * @param relativePath
	 * @return
	 */
	private static File getDirectory(String relativePath) {
		File result = null;
		
		if (TextUtils.isEmpty(relativePath)) {
			Logger.e("Try to access a directory with empty relative path!");
			return result;
		}
		
		if (AndroidFileUtils.isExternalStorageWritable()) {
			result = AndroidFileUtils.getOrCreateDirectoryOnExternalStorage(relativePath);
		} else {
			result = AndroidFileUtils.getOrCreateDirectoryOnInternalStorage(GlobalApplication.getInstance(), relativePath);
		}
		
		return result;
	}
	
	
	
	

}
