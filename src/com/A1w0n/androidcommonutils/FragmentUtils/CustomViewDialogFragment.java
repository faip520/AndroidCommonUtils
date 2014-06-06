package com.A1w0n.androidcommonutils.FragmentUtils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.faip.androidcommonutils.R;

public class CustomViewDialogFragment extends DialogFragment {

	public CustomViewDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity());

		dialog.setTitle("请输入想要修改的文字：");

		// dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.custom_view_dialogfragment);
		return dialog;
	}
}
