package com.A1w0n.androidcommonutils.DialogUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
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

    /**
     * 想安全的关闭一个Dialog，即使它所依附的Activity已经关闭了，分两种情况：
     * 如果Activity是你自己的Activity：那就在onDestroy里头把Dialog关闭掉，然后调用这个方法来dismiss，就可以了，并且不会有Exception
     * 如果Activity不是你的自己的Activity，代码无法修改，也用这个方法，但是有可能出现Exception
     */
    public static void dismissDialogSafely(Dialog dialog) {
        try {
            if (dialog != null && dialog.isShowing() && dialog.getWindow() != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
