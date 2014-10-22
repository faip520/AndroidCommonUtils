package com.A1w0n.androidcommonutils.AsyncTaskUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Aiwan on 2014/5/24 0024.
 * 带有ProgressDialog的AsyncTask，可以调用updateProgressDialogMessage来更新显示的提示信息
 * 执行完会自动dismissDialog
 */
public abstract class PreDialogAsyncTask<Params, Progress, Result> extends BaseAsyncTask<Params, Progress, Result> implements DialogInterface.OnCancelListener {

	protected ProgressDialog mProgress;

	private Context mContext;
	
	private String mPreMessage;

	/**
	 * @param preMessage ProgressDialog一开始要显示的提示
	 */
	protected PreDialogAsyncTask(Context context, String preMessage) {
		this.mContext = context;
		mPreMessage = preMessage;
	}

	@Override
	protected void PreExecute() {
		mProgress = new ProgressDialog(mContext);
		mProgress.setIndeterminate(true);
		mProgress.setCancelable(false);
		mProgress.setMessage(mPreMessage);
		mProgress.setOnCancelListener(PreDialogAsyncTask.this);
		mProgress.show();
		mContext = null;
	}
	
	@Override
	protected void PostExecute(Result paramResult) {
		super.PostExecute(paramResult);
		dismissProgressDialogInner();
	}

	protected void updateProgressDialogMessage(final String message) {
		if (isUiThread())
			updateMessageInner(message);
		else
			sHandler.post(new Runnable() {
				public void run() {
					updateMessageInner(message);
				}
			});
	}
	
	private void updateMessageInner(String message) {
		if (mProgress != null && mProgress.isShowing()) {
			mProgress.setMessage(message);
		}
	}
	
	private void dismissProgressDialogInner() {
		if (mProgress != null && mProgress.isShowing() && mProgress.getWindow() != null) {
			mProgress.dismiss();
		}
	}
	
	protected void dismissProgressDialog() {
		if (isUiThread())
			dismissProgressDialogInner();
		else
			sHandler.post(new Runnable() {
				public void run() {
					dismissProgressDialogInner();
				}
			});
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
	}
}
