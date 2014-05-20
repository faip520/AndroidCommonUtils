package com.faip.androidcommonutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class CommonAsyncTask extends AsyncTask<Uri, Void, Bitmap> implements OnCancelListener {
	
	private ProgressDialog mProgress;
	
	private Uri mUri;
	
	private Context mContext;
	
	public CommonAsyncTask(Context context) {
		mContext = context;
	}

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
	protected Bitmap doInBackground(Uri... params) {
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
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
