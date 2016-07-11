package com.A1w0n.androidcommonutils.FragmentUtils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
		// setCancelable(false);也可以实现同样的功能，但是这样你按返回键就不可以取消dialog了
		dialog.setCanceledOnTouchOutside(false);

		return super.onCreateDialog(savedInstanceState);
	}

	/**
	 * 显示这个Dialog的接口
	 *
	 * @param targetUid 对方的uid
	 */
	public static void showFragment(FragmentManager fm, String targetUid) {
		if (fm == null || TextUtils.isEmpty(targetUid)) {
			return;
		}

		BaseDialogFragment df = new BaseDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("uid", targetUid);
		df.setArguments(bundle);

		FragmentTransaction ft = fm.beginTransaction();

		// 删除之前已有的
		Fragment pres = fm.findFragmentByTag(TAG);
		if (pres != null) {
			ft.remove(pres);
		}

		// 不加这个就不能返回键取消Fragment
		ft.addToBackStack(TAG);
		df.show(ft, TAG);
	}
}
