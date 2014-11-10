package com.A1w0n.androidcommonutils.AsyncTaskUtils;

import java.io.File;
import java.io.IOException;

import com.A1w0n.androidcommonutils.FileUtils.JavaFileUtils;

import android.R.bool;
import android.content.Context;

public class CopyFileAsyncTask extends PreDialogAsyncTask<Void, Void, Boolean> {
	
	private File mSrcFile;
	private File mDestFile;

	protected CopyFileAsyncTask(Context context, String preMessage) {
		super(context, "正在拷贝...");
	}
	
	public void setArgs(File src, File dest) {
		mSrcFile = src;
		mDestFile =dest;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			JavaFileUtils.copyFile(mSrcFile, mDestFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
