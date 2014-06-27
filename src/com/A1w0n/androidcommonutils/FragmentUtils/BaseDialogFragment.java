package com.A1w0n.androidcommonutils.FragmentUtils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.view.Window;

public class BaseDialogFragment extends DialogFragment {
	
	public static final String TAG = "BaseDialogFragment";

	public BaseDialogFragment() {
		// 显示系统的一切的Fragment相关的操作信息
		FragmentManager.enableDebugLogging(true);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity());
		// 不显示标题
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// 刚好Wrap_content
		int dialogWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
		int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setLayout(dialogWidth, dialogHeight);
		// 设置背景
		//dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round_corner);
		// 点击外部不能取消
		setCancelable(false);
		
		return super.onCreateDialog(savedInstanceState);
	}
	
	/**
	 * 显示这个Dialog的接口
	 */
	public void showDialog(FragmentManager fm) {
		SimpleTwoChoiceDialogFragment df = new SimpleTwoChoiceDialogFragment();
		df.show(fm, TAG);
	}
}
