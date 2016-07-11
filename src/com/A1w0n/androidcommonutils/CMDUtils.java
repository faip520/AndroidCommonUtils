package com.A1w0n.androidcommonutils;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.concurrent.BrokenBarrierException;

import android.text.TextUtils;
import android.util.EventLog;

import com.A1w0n.androidcommonutils.IOUtils.IOUtils;
import com.A1w0n.androidcommonutils.DebugUtils.Logger;

public class CMDUtils {

	private CMDUtils() {
	}

	public static boolean runWithRoot(String command) {
		int result = -1;
		
		Process process = null;
		DataOutputStream os = null;
		InputStream is = null;
		InputStream es = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			result = process.waitFor();
			Logger.d("Run command : " +command + ", process return value : " + result);
			is = process.getInputStream();
			es = process.getErrorStream();
			Logger.d(IOUtils.toString(process.getInputStream()));
			Logger.e(IOUtils.toString(process.getErrorStream()));
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeSilently(os);
			IOUtils.closeSilently(is);
			IOUtils.closeSilently(es);
			if (process != null) {
				process.destroy();
			}
		}
		
		return result == 0;
	}
	
	public static boolean runWithoutRoot(String command) {
		int result = -1;
		
		Process process = null;
		DataOutputStream os = null;
		InputStream is = null;
		InputStream es = null;
		try {
			process = Runtime.getRuntime().exec("sh");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			result = process.waitFor();
			Logger.d("Run command : " +command + ", process return value : " + result);
			is = process.getInputStream();
			es = process.getErrorStream();
			Logger.d(IOUtils.toString(process.getInputStream()));
			Logger.e(IOUtils.toString(process.getErrorStream()));
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeSilently(os);
			IOUtils.closeSilently(is);
			IOUtils.closeSilently(es);
			if (process != null) {
				process.destroy();
			}
		}
		
		return result == 0;
	}
	
	/**
	 * 非root权限下顺序执行多个命令
	 * @param commands
	 * @return
	 */
	public static boolean runWithoutRoot(String[] commands) {
		int result = -1;
		
		if (commands == null || commands.length == 0) {
			Logger.e("Illegal argument!");
			return false;
		}
		
		Process process = null;
		DataOutputStream os = null;
		InputStream is = null;
		InputStream es = null;
		try {
			process = Runtime.getRuntime().exec("sh");
			os = new DataOutputStream(process.getOutputStream());
			
			for (int i = 0; i < commands.length; i++) {
				if (!TextUtils.isEmpty(commands[i])) {
					os.writeBytes(commands[i] + "\n");
					Logger.d("Run command : " + commands[i]);
				}
			}
			os.writeBytes("exit\n");
			os.flush();
			result = process.waitFor();
			Logger.d("Run commands process return value : " + result);
			is = process.getInputStream();
			es = process.getErrorStream();
			Logger.d(IOUtils.toString(process.getInputStream()));
			Logger.e(IOUtils.toString(process.getErrorStream()));
		} catch (Exception e) {
			return false;
		} finally {
            IOUtils.closeSilently(os);
            IOUtils.closeSilently(is);
            IOUtils.closeSilently(es);
            if (process != null) {
                process.destroy();
            }
        }

        return result == 0;
	}
	
	/**
	 * 通过shell命令，kill掉指定名字的进程，Android里apk的进程名字就是它的包名
	 * 需要root权限的！
	 * @param processName
	 * @return
	 */
	public static boolean killProcessByName(String processName) {
		if (TextUtils.isEmpty(processName)) {
			Logger.e("Illegal argument!");
			return false;
		}
		return runWithRoot("set `ps | grep " + processName + "` && kill $2");
	}
}
