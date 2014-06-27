package com.A1w0n.androidcommonutils;

import java.io.DataOutputStream;

import com.A1w0n.androidcommonutils.IOUtils.IOUtils;
import com.A1w0n.androidcommonutils.debugutils.Logger;

public class CMDUtils {

	private CMDUtils() {
	}

	public static boolean RootCommand(String command) {
		int result = -1;
		
		Process process = null;
		DataOutputStream os = null;
		try {
			String[] cmdStrings = new String[] { "sh", "-c", "su" };
			process = Runtime.getRuntime().exec(cmdStrings);
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			Logger.d("command = " + command);
			os.writeBytes("exit\n");
			os.flush();
			result = process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeSilently(os);
			process.destroy();
		}
		
		return result == 1;
	}

}
