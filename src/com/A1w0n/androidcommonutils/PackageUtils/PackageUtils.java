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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.A1w0n.androidcommonutils.CMDUtils;
import com.A1w0n.androidcommonutils.FileUtils.A1w0nFileManager;
import com.A1w0n.androidcommonutils.IOUtils.IOUtils;
import com.A1w0n.androidcommonutils.JniUtils.Exec;
import com.A1w0n.androidcommonutils.bitmaputils.BitmapUtils;
import com.A1w0n.androidcommonutils.debugutils.Logger;

public class PackageUtils {

	private PackageUtils() {
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
		CMDUtils.RootCommand(movPath);
		CMDUtils.RootCommand("chmod 777 /data/" + apkFileName);
		CMDUtils.RootCommand("mv data/" + apkFileName + " /data/app/");
		int time = 1;
		CMDUtils.RootCommand("echo  " + time + "  >/sys/class/led/device/led_time");
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
			item.thumbnailPath = syncSavePackageIcon(context, itemInfo.packageName, BitmapUtils.getBitmap(itemInfo.loadIcon(pm)));
			item.apkPath = itemInfo.sourceDir;
			item.title = itemInfo.loadLabel(pm).toString();
			item.packName = itemInfo.packageName;
			item.versionCode = p.versionCode;
			item.versionName = p.versionName;
			// 
			File file = A1w0nFileManager.getIconFileForWriting(
					String.valueOf(itemInfo.packageName.hashCode()));
			item.fileSize = file.length();
			returnMap.add(item);
		}
		return returnMap;
	}
	
	/**
	 * 把icon 对应的bitmap文件保存起来
	 * @param context
	 * @param packageName
	 * @param bitmap
	 * @return
	 */
	public static String syncSavePackageIcon(Context context, final String packageName, final Bitmap bitmap) {
		String iconFileName = String.valueOf(packageName.hashCode());
		File file = A1w0nFileManager.getIconFileForWriting(iconFileName);
		BitmapUtils.saveToFile(file, bitmap);
		return file.getAbsolutePath();
	}
	
	/**
	 * 通过删除/data/app下的apk文件，和/data/data/packagename文件夹 方式卸载软件
	 * 注意：本方法只适用于dataAppsilentInstall方式安装的软件
	 */
	public static void dataAppSlientUninstall(String packageName) {
		CMDUtils.RootCommand("rm -rf " + "/data/data/" + packageName);
		CMDUtils.RootCommand("rm -rf " + "/data/app/" + packageName + ".apk");
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

}
