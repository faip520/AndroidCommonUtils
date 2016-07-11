package com.A1w0n.androidcommonutils.DialogUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 *
 *
 * Created by A1w0n on 14/12/22.
 */
public class SafeDialog extends Dialog {
    private WeakReference mContext = null;

    public SafeDialog(Context var1) {
        super(var1);
        this.mContext = new WeakReference(var1);
    }

    public SafeDialog(Context var1, int var2) {
        super(var1, var2);
        this.mContext = new WeakReference(var1);
    }

    protected SafeDialog(Context var1, boolean var2, OnCancelListener var3) {
        super(var1, var2, var3);
        this.mContext = new WeakReference(var1);
    }

    public boolean isActivityFinishing() {
        Context var1 = (Context)this.mContext.get();
        return var1 instanceof Activity && ((Activity)var1).isFinishing();
    }

    public void show() {
        if(!this.isActivityFinishing()) {
            super.show();
        }
    }
}
