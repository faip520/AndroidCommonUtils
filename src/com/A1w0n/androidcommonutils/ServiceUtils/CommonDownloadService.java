package com.A1w0n.androidcommonutils.ServiceUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.faip.androidcommonutils.R;

/**
 * Service虽然没有界面，但是默认运行在UI线程上
 */
public class CommonDownloadService extends Service {
	
	private static final int ONGOING_NOTIFICATION_ID = 10086;
	
	// 用来返回给调用者的Binder对象
	private CommonDownloadBinder mBinder;
	
	// 用来显示通知栏信息
	private NotificationManager mNotificationManager;
	private Notification mNotification;

	@Override
	public void onCreate() {
		super.onCreate();
		
		mBinder = new CommonDownloadBinder();
		mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		// 前台Service没那么容易被kill掉
//		Notification notification = new Notification(R.drawable.ic_launcher, "On Going",
//		        System.currentTimeMillis());
//		Intent notificationIntent = new Intent(this, Activity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//		notification.setLatestEventInfo(this, getText(R.string.notification_title),
//		        getText(R.string.notification_message), pendingIntent);
//		startForeground(ONGOING_NOTIFICATION_ID, notification);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// 去掉On Going的通知...如果设置成了前台Service
		//stopForeground(true);
	}

	/**
	 * 返回一个CommonDownloadBinder给Service的调用者，让它可以使用CommonDownloadBinder
	 * 中定义的方法。
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	/**
	 * 创建通知
	 */
	private void setUpNotification() {
//		int icon = R.drawable.ic_launcher;
//		CharSequence tickerText = "开始下载";
//		long when = System.currentTimeMillis();
//		mNotification = new Notification(icon, tickerText, when);  
////		mNotification = new Notification.Builder(mContext)
////        .setContentTitle(tickerText)
////        .setContentText(""+when)
////        .setSmallIcon(icon)
////        .build();
//
//		// 放置在"正在运行"栏目中
//		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
//
//		RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.download_notification_layout);
//		contentView.setTextViewText(R.id.fileName, "正在上传文件:" + mUploadingParams.title);
//		// 指定个性化视图
//		mNotification.contentView = contentView;
//
//		Intent intent = new Intent(this, FileMgrActivity.class);
//		intent.putExtra("title", mUploadingParams.title);
//		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		// 指定内容意图
//		mNotification.contentIntent = contentIntent;
//		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}
	
	

	// =====================CommonDownloadBinder=========================

	/**
	 * CommonDownloadBinder中定义了一些实用的方法
	 */
	public class CommonDownloadBinder extends Binder {

		public void cancel() {
		}

		public void isCancelled() {
		}

		public void getDownloadProgress() {
		}

		public void addDownloadTask() {
		}

		public void startUploadTask() {
		}

	}

	// ==========================End of CommonDownloadBinder==================

}
