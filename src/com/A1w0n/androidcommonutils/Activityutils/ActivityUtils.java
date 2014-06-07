package com.A1w0n.androidcommonutils.Activityutils;

import android.app.Activity;
import android.content.Intent;

public class ActivityUtils {

	private ActivityUtils() {
	}
	
	/**
	 * Start the activity to pick an image from the user gallery
	 */
	public static void pickFromGallery(Activity activity) {
		Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
		intent.setType( "image/*" );

		Intent chooser = Intent.createChooser( intent, "Choose a Picture" );
		int requestCode = 10086;
		activity.startActivityForResult( chooser, requestCode );
	}
}
