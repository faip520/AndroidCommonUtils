package com.A1w0n.androidcommonutils.FragmentUtils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 *
 * Created by A1w0n on 14/12/11.
 */
public class FragmentUtils {

    /**
     * Global method to show dialog fragment including adding it to back stack Note: DO NOT call this from an async
     * task! If you need to show a dialog from an async task, use showAsyncDialogFragment()
     *
     * @param newFragment  the DialogFragment you want to show
     */
    public void showDialogFragment(DialogFragment newFragment, FragmentManager fm) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        // save transaction to the back stack
        ft.addToBackStack("dialog");
        newFragment.show(ft, "dialog");
    }

    /**
     * 判断一个fragment是否曾经被添加到某个activity或者另一个fragment
     *
     * @param fragment
     * @return
     */
    public static boolean isFragmentBeenAdded(Fragment fragment) {
        return fragment.isAdded();
    }

    /**
     * 操作fragmentManager的标准流程
     *
     * @param fm
     */
    public static void commitFragmentTransaction(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();

        if (!ft.isEmpty()) {
            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
