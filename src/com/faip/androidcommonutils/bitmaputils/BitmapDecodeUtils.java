package com.faip.androidcommonutils.bitmaputils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

public class BitmapDecodeUtils {

	private BitmapDecodeUtils() {
	}

	public static Bitmap decode(Context context, Uri uri, int maxW, int maxH) {
		Bitmap bitmap = null;

		int requiredSize = Math.min(maxW, maxH);
		bitmap = BitmapUtils.decreaseImageQualityByRequiredSize(context, uri, requiredSize);
		
		return bitmap;
	}



}
