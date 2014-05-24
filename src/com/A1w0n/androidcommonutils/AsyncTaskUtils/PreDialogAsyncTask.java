package com.A1w0n.androidcommonutils.AsyncTaskUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Aiwan on 2014/5/24 0024.
 */
public abstract class PreDialogAsyncTask<Params, Progress, Result> extends BaseAsyncTask<Params, Progress, Result> implements DialogInterface.OnCancelListener {

    protected ProgressDialog mProgress;

    private Context mContext;

    protected PreDialogAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void PreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(true);
        mProgress.setMessage("Loading...");
        mProgress.setOnCancelListener(PreDialogAsyncTask.this);
        mProgress.show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
    }
}
