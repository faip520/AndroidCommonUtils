package com.A1w0n.androidcommonutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public class CommonAsyncTask extends AsyncTask<Integer, Void, Void> implements OnCancelListener {
	
	// So that users can access and show this progress dialog.
	private ProgressDialog mProgress;
	
	private Context mContext;
	private Handler uiHandler = new Handler(Looper.getMainLooper());
	
	public CommonAsyncTask(Context context) {
		mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				// Must pass in a activity instance.
				mProgress = new ProgressDialog(mContext);
				mProgress.setIndeterminate(true);
				mProgress.setCancelable(true);
				mProgress.setMessage("Loading...");
				mProgress.setOnCancelListener(CommonAsyncTask.this);
				mProgress.show();
			}
		});
		
		mContext = null;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		return null;
	}
	
	/**
	 * Make it abstract, let the subclass to implements it. Or u will get a exception saying you touch the view in a 
	 * wrong thread.
	 */
	protected void updateProgressDialogHint(final String hint) {
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mProgress != null && mProgress.isShowing()) {
					mProgress.setMessage(hint);
				}
			}
		});
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mProgress.getWindow() != null) {
					mProgress.dismiss();
				}
			}
		});
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
