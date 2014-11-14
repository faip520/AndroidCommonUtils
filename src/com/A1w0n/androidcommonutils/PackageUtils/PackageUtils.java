package com.A1w0n.androidcommonutils.PackageUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.A1w0n.androidcommonutils.CMDUtils;
import com.A1w0n.androidcommonutils.BitmapUtils.BitmapUtils;
import com.A1w0n.androidcommonutils.FileUtils.ProjectFileManager;
import com.A1w0n.androidcommonutils.IOUtils.IOUtils;
import com.A1w0n.androidcommonutils.JniUtils.Exec;
import com.A1w0n.androidcommonutils.debugutils.Logger;

public class PackageUtils {

	private PackageUtils() {
	}
	
	
	/**
	 * 仅仅用于备忘，别调用这个！
	 * @param context
	 * @return
	 */
	public static String getMyPackageName(Application context) {
		return context.getPackageName();
	}

	/**
	 * 静默安装
	 * 
	 * @param file
	 * @return 
	 *     安装结果
	 */
	public static boolean slientInstall(File file) {
		if (file == null || !file.exists()) {
			Logger.e("尝试安装一个不存在的文件");
			return false;
		}
		
		Logger.d("开始安装文件" + file.getAbsolutePath());
		
		boolean result = false;
		Process process = null;
		OutputStream out = null;
		try {
			process = Runtime.getRuntime().exec("su");
			out = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			//dataOutputStream.writeBytes("chmod 777 " + file.getAbsolutePath() + "\n");
			dataOutputStream.writeBytes("pm install -r " + file.getAbsolutePath() + " \n");
			// 提交命令
			dataOutputStream.flush();
			// 关闭流操作
			dataOutputStream.close();
			out.close();
			int value = process.waitFor();
			
			Logger.d("===" + value + "  " + file.getAbsolutePath());

			// 代表成功
			if (value == 0) {
				result = true;
			} else if (value == 1) { // 失败
				result = false;
			} else { // 未知情况
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		process.destroy();
		return result;
	}
	
	public static boolean uninstallPackage(String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			Logger.e("Try to uninstall a empty package!");
		}
		
		boolean result = false;
		
		Process process = null;
		OutputStream out = null;
		try {
			process = Runtime.getRuntime().exec("su");
			out = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			//dataOutputStream.writeBytes("chmod 777 " + file.getAbsolutePath() + "\n");
			dataOutputStream.writeBytes("pm uninstall " + packageName + " \n");
			// 提交命令
			dataOutputStream.flush();
			// 关闭流操作
			dataOutputStream.close();
			out.close();
			int value = process.waitFor();
			
			// 代表成功
			if (value == 0) {
				result = true;
			} else if (value == 1) { // 失败
				result = false;
			} else { // 未知情况
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		process.destroy();
		return result;
	}
	
	/**
	 * 如果已经root，把app直接拷到/data/app/下就能自动安装
	 * 注意 ： 有的手机，要修改apk文件权限后才能自动安装
	 */
	public static void dataAppSilentInstall(File apkFile) {
		if (apkFile == null || !apkFile.exists()) {
			Logger.e("Try to install error file!");
			return;
		}
		
		String apkFileName = apkFile.getName();
		
		String path = apkFile.getAbsolutePath();
		String movPath = "cp   " + path + " data/" + apkFileName;
		CMDUtils.runWithRoot(movPath);
		CMDUtils.runWithRoot("chmod 777 /data/" + apkFileName);
		CMDUtils.runWithRoot("mv data/" + apkFileName + " /data/app/");
		int time = 1;
		CMDUtils.runWithRoot("echo  " + time + "  >/sys/class/led/device/led_time");
	}
	
	public static List<App> getAllApk(Context context) {
		List<App> returnMap = new ArrayList<App>();
		PackageManager pm = context.getPackageManager();
		final Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
		int count = packs.size() - 1;
		for (int i = 0; i <= count; i++) {
			PackageInfo p = packs.get(i);
			if (p.versionName == null) {
				continue;
			}
			
			ApplicationInfo itemInfo = p.applicationInfo;
			if ((itemInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
				continue;
			App item = new App();
//			item.userName = Settings.getUserName(mContext);
//			item.macAddress = Settings.getMacAddress(mContext);
			String packageName = itemInfo.packageName;
			
			File iconFile = ProjectFileManager.getIconFileForWriting(packageName);
			if (iconFile.exists() && iconFile.length() != 0) {
				Logger.e("Icon file of package name: " + packageName + " already existed!");
			} else {
				BitmapUtils.saveToFile(iconFile, BitmapUtils.getBitmap(itemInfo.loadIcon(pm)));
			}
			
			item.thumbnailPath = iconFile.getAbsolutePath();
			item.apkPath = itemInfo.sourceDir;
			item.title = itemInfo.loadLabel(pm).toString();
			item.packName = packageName;
			item.versionCode = p.versionCode;
			item.versionName = p.versionName;
			item.fileSize = iconFile.length();
			returnMap.add(item);
		}
		return returnMap;
	}
	
	/**
	 * 通过删除/data/app下的apk文件，和/data/data/packagename文件夹 方式卸载软件
	 * 注意：本方法只适用于dataAppsilentInstall方式安装的软件
	 */
	public static void dataAppSlientUninstall(String packageName) {
		CMDUtils.runWithRoot("rm -rf " + "/data/data/" + packageName);
		CMDUtils.runWithRoot("rm -rf " + "/data/app/" + packageName + ".apk");
	}
	
	/**
	 * 通过Jni层c++代码来执行pm命令，从而达到静默安装的目的
	 */
	public static void installSlientlyJni() {
		String[] args = new String[] { "su", "-c", "pm install -r /mnt/sdcard/aa.apk" };

		String[] envVars = new String[3];
		envVars[0] = "TERM=screen";
		envVars[1] = "PATH=/vendor/bin:/system/bin:/system/xbin";
		envVars[2] = "HOME=/data/data/jackpal.androidterm/app_HOME";

		int[] processId = new int[1];

		// 一般都在xbin里头，在/system/bin里头即使有，也只是一个链接
		String suLocation = "/system/xbin/su";
		
		FileDescriptor fd = Exec.createSubprocess(suLocation, args, envVars, processId);

		 OutputStream out = new FileOutputStream(fd);
		
		 Logger.d("============start to write==========");
		 try {
		 out.write("cp /mnt/sdcard/aa.apk /data/".getBytes());
		 out.flush();
		 Logger.d("============writer to term success==========");
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
		 IOUtils.closeSilently(out);

		Logger.d("============start to wait children=====process id = " + processId[0]);
		Exec.waitFor(processId[0]);
		Logger.d("============end of wait children==========");

		Logger.d("============start to new  FileInputStream==========");
		FileInputStream is = new FileInputStream(fd);

		char[] message = new char[4096];
		int index = 0;
		try {
			Logger.d("============new FileInputStream success====available length : ======" + is.available());
			int s;
			while ((s = is.read()) != -1) {
				Logger.d("======read a byte from term : " + ((char) s));
				message[index] = ((char) s);
				index++;

				if (index == 4096) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			String ms = new String(message);
			System.out.print("============read from term success ====message : " + ms);
		}

		IOUtils.closeSilently(is);

		Exec.close(fd);
	}
	
	/**
	 * 从文件系统中的apk文件中获取包名，其实这里已经可以获取到PackageInfo对象了
	 * 理论上关于应用软件的一切信息都能获取到了
	 * @param context
	 * @param absPath
	 * @return
	 */
	public static String getApkFilePackageName(Context context, String absPath) {
		String result = null;
		
		if (context == null || TextUtils.isEmpty(absPath)) {
			Logger.e("Illegal argument!");
			return result;
		}
		
	    PackageManager pm = context.getPackageManager();  
	    PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath,PackageManager.GET_ACTIVITIES);  
	    if (pkgInfo != null) {
	    	result = pkgInfo.packageName;
	    }  
		
		return result;
	}
	
	/**
	 * 获取apk文件对应的应用的icon
	 * @param context
	 * @param absPath
	 * @return
	 */
	public static Drawable getApkFileIconDrawable(Context context, String absPath) {
		Drawable result = null;
		
		if (context == null || TextUtils.isEmpty(absPath)) {
			Logger.e("Illegal argument!");
			return result;
		}
		
	    PackageManager pm = context.getPackageManager();  
	    PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath,PackageManager.GET_ACTIVITIES);  
	    if (pkgInfo != null) {
	        ApplicationInfo appInfo = pkgInfo.applicationInfo;
	        // 必须加这两句，不然下面icon获取是default icon而不是应用包的icon
	        appInfo.sourceDir = absPath;  
	        appInfo.publicSourceDir = absPath;
	        
	        // 这两个获取到的应该是一样的
	        result = pm.getApplicationIcon(appInfo);
	        //result = appInfo.loadIcon(pm);
	    }  
		
		return result;
	}
	
	/**
	 * 给定包名，返回这个包名对应的软件是否已经安装了
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstalled(Context context, String packageName) {
		if (context == null || TextUtils.isEmpty(packageName)) {
			Logger.e("Illegal argument!");
			return false;
		}
		
		PackageManager pm = context.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		
		return app_installed ;
	}
	
	/**
	 * 获取正在运行的软件包名
	 * @param context
	 * @return
	 */
	public static String currentRunningPackageName(Context context) {
		if (context == null) {
			Logger.e("Illegal argument!");
			return null;
		}
		
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> mTaskInfo = mActivityManager.getRunningTasks(1);
		ComponentName componentInfo = mTaskInfo.get(0).topActivity;
		return componentInfo.getPackageName();
	}
	
	/**
	 * 给出包名，启动包名对应的应用软件的主Activity
	 * @param context
	 * @param packageName
	 */
	public static void startMainActivity(Context context, String packageName) {
		if (context == null || TextUtils.isEmpty(packageName) || packageName.length() > 200) {
			Logger.e("Illegal arguments!");
			return;
		}
		
		context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
	}
	
}
