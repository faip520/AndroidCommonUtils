package com.A1w0n.androidcommonutils.AsyncTaskUtils;

import java.io.File;

import com.A1w0n.androidcommonutils.CMDUtils;
import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;
import com.A1w0n.androidcommonutils.PackageUtils.PackageUtils;
import com.A1w0n.androidcommonutils.debugutils.Logger;

public class InstallApkAsyncTask extends BaseAsyncTask<File, String, Boolean> {
	
	@Override
	protected Boolean doInBackground(File... params) {
		if (params == null || params.length <= 0 || params[0] == null
				|| !params[0].exists()) {
			Logger.e("Illegal argument!");
			return false;
		}
		
		String absPath = params[0].getAbsolutePath();
		String packageName = PackageUtils.getApkFilePackageName(
				GlobalApplication.getInstance(),
				absPath);
		
		if (PackageUtils.isInstalled(GlobalApplication.getInstance(), packageName)) {
			Logger.d("Apk file already installed!");
			return true;
		}
		
		return CMDUtils.runWithoutRoot("a1w0n -c \"pm install -r " + params[0].getAbsolutePath() + "\"");
	}

}
