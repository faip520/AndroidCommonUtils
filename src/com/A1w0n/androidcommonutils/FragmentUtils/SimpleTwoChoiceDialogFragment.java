package com.A1w0n.androidcommonutils.FragmentUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * 一个显示一个Title和两个选择的简洁的DialogFragment
 */
public  class SimpleTwoChoiceDialogFragment extends DialogFragment {
	
	public static final String TAG_SimpleTwoChoiceDialogFragment = "tag_stcdf";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Title").setItems(new String[] {"Item1", "Item2"}, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// do click action.
			}
		});
		return builder.create();
	}
	
	/**
	 * 这个是显示这个Dialog的接口
	 */
	public void showDialog(FragmentManager fm) {
		SimpleTwoChoiceDialogFragment df = new SimpleTwoChoiceDialogFragment();
		df.show(fm, TAG_SimpleTwoChoiceDialogFragment);
	}
}