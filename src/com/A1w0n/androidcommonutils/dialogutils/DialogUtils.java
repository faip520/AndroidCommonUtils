package com.A1w0n.androidcommonutils.dialogutils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

public class DialogUtils {
	
	/**
	 * 一个没有Title只有Message 并且底部有左右两个按钮的Dialog的示例
	 * @param context
	 */
	public static void alertDialogExample(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("请先完善个人资料，设置头像，昵称和性别，诚心交友");
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		
		builder.setPositiveButton("完善资料", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		
		// Must call show() prior to fetching text view
		// 文本默认情况下不是居中的，这里尝试让它居中显示
		TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
		if (messageView != null) {
			messageView.setGravity(Gravity.CENTER);
		}
	}

}
