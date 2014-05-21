package com.faip.androidcommonutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class CommonAsyncTask extends AsyncTask<Integer, Void, Void> implements OnCancelListener {
	
	// So that users can access and show this progress dialog.
	protected ProgressDialog mProgress;
	
	private Context mContext;
	
	public CommonAsyncTask() {
	}

	// Let the user implements this function, so it doesn't need a context here.
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mProgress = new ProgressDialog(mContext);
		mProgress.setIndeterminate(true);
		mProgress.setCancelable(true);
		mProgress.setMessage("Loading...");
		mProgress.setOnCancelListener(this);
		mProgress.show();
		
		mContext = null;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		return null;
	}
	
	protected void updateProgressDialogHint(String hint) {
		if (mProgress != null && mProgress.isShowing()) {
			mProgress.setMessage(hint);
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (mProgress.getWindow() != null) {
			mProgress.dismiss();
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
}
