package com.A1w0n.androidcommonutils.AsyncTaskUtils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by Aiwan on 2014/5/24 0024.
 * 所有AsynaTask的基类，确保三个主要的回调函数都在UI线程运行，防止
 * 在这些回调里操作UI控件出现各种错误。
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private boolean isUiThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    protected void onPreExecute() {
        if (isUiThread())
            PreExecute();
        else
            sHandler.post(new Runnable() {
                public void run() {
                    BaseAsyncTask.this.PreExecute();
                }
            });
    }

    protected final void onPostExecute(final Result result) {
        if (isUiThread())
            PostExecute(result);
        else
            sHandler.post(new Runnable() {
                public void run() {
                    PostExecute(result);
                }
            });
    }

    protected void onProgressUpdate(final Progress... values) {
        if (isUiThread())
            ProgressUpdate(values);
        else
            sHandler.post(new Runnable() {
                public void run() {
                    BaseAsyncTask.this.ProgressUpdate(values);
                }
            });
    }

    protected void PostExecute(Result paramResult) {};

    protected void PreExecute() {};

    protected void ProgressUpdate(Progress... values) {
    }
}
