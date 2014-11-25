package com.A1w0n.androidcommonutils.AsyncImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;
import com.A1w0n.androidcommonutils.IOUtils.IOUtils;
import com.A1w0n.androidcommonutils.R;
import com.A1w0n.androidcommonutils.ViewUtils.ViewUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

	private static ImageLoader mInstance;

	private Drawable mStubDrawable;

	MemoryCache memoryCache = new MemoryCache();

	FileCache fileCache;

	// Thread pool to download phots.
	ExecutorService executorService;

	// Handler to display images in UI thread
	Handler handler = new Handler();

	private ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		mStubDrawable = context.getResources().getDrawable(R.drawable.empty_frame);
	}

	public static ImageLoader getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new ImageLoader(context);
		}

		return mInstance;
	}

	/**
	 * Display the default image if cannot find bitmap in cache, and start a task to downlaod the
	 * real image and display it when finished.
	 * This is for ImageView only. Use displayImage(String url, View page) for other views.
	 */
	public void displayImage(String url, ImageView imageView) {
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView);
			imageView.setImageDrawable(mStubDrawable);
		}
	}
	
	/**
	 * Use to set background for any view.
	 */
	public void displayImage(String url, View page) {
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			ViewUtils.setBackground(page, bitmap, GlobalApplication.getInstance());
		} else {
			queuePhoto(url, page);
			ViewUtils.setBackground(page, mStubDrawable);
		}
	}
	
	/**
	 * Post a image download task to thread pool.
	 */
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}
	
	private void queuePhoto(String url, View page) {
		PageBGToLoad p = new PageBGToLoad(url, page);
		executorService.submit(new PageBGLoader(p));
	}

	/**
	 * Get bitmap by url. Bitmaps cached in local file system.
	 */
	private Bitmap getBitmap(String url) {
		// create a corresponding cache file in cache directory.
		File f = fileCache.getFile(url);

		// get bitmap from cache file.
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// get bitmap from web if getting from file fails.
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			IOUtils.copy(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}
	
	/**
	 * Get bitmap by url. Bitmaps cached in local file system.
	 */
	private Bitmap getBitmap(String url, int requiredSize) {
		// create a corresponding cache file in cache directory.
		File f = fileCache.getFile(url);

		// get bitmap from cache file.
		Bitmap b = decodeFile(f, requiredSize);
		if (b != null)
			return b;

		// get bitmap from web if getting from file fails.
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			IOUtils.copy(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f, requiredSize);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	/**
	 * Decodes image file to bitmap and scales it to reduce memory consumption
	 */
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			o.inPurgeable = true;
			o.inInputShareable = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			// Caculate the scale factor.
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Decodes image file to bitmap and scales it to reduce memory consumption
	 */
	private Bitmap decodeFile(File f, int requiredSize) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = requiredSize;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			// Caculate the scale factor.
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inInputShareable = true;
			o2.inPurgeable = true;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ==========Task for the queue===============
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	// =====================================
	
	// ==========Task for the queue===============
	private class PageBGToLoad {
		public String url;
		public View wysPage;

		public PageBGToLoad(String u, View i) {
			url = u;
			wysPage = i;
		}
	}

	// =====================================

	// ===================PhotosLoader==========================
	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				Bitmap bmp = getBitmap(photoToLoad.url);
				memoryCache.put(photoToLoad.url, bmp);

				if (bmp == null)
					return;

				TransitionDrawable td = null;
				Drawable[] drawables = new Drawable[2];
				drawables[0] = mStubDrawable;
				drawables[1] = new BitmapDrawable(GlobalApplication.getInstance().getResources(), bmp);
				td = new TransitionDrawable(drawables);
				td.setCrossFadeEnabled(true);

				BitmapDisplayer bd = new BitmapDisplayer(td, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	// ==================================================
	
	// ===================PhotosLoader==========================
	class PageBGLoader implements Runnable {
		PageBGToLoad pageBGToLoad;

		PageBGLoader(PageBGToLoad pageBGToLoad) {
			this.pageBGToLoad = pageBGToLoad;
		}

		@Override
		public void run() {
			try {
				Bitmap bmp = getBitmap(pageBGToLoad.url, 400);
				memoryCache.put(pageBGToLoad.url, bmp);

				if (bmp == null)
					return;

				TransitionDrawable td = null;
				Drawable[] drawables = new Drawable[2];
				drawables[0] = mStubDrawable;
				drawables[1] = new BitmapDrawable(GlobalApplication.getInstance().getResources(), bmp);
				td = new TransitionDrawable(drawables);
				td.setCrossFadeEnabled(true);

				PageBGBitmapDisplayer bd = new PageBGBitmapDisplayer(td, pageBGToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	// ==================================================

	// ================BitmapDisplayer=================
	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		TransitionDrawable mTD;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(TransitionDrawable td, PhotoToLoad p) {
			mTD = td;
			photoToLoad = p;
		}

		public void run() {
			if (mTD != null) {
				photoToLoad.imageView.setImageDrawable(mTD);
				mTD.startTransition(400);
			} else
				photoToLoad.imageView.setImageDrawable(mStubDrawable);
		}
	}

	// ===============================================
	
	// ================BitmapDisplayer=================
	// Used to display bitmap in the UI thread
	class PageBGBitmapDisplayer implements Runnable {
		TransitionDrawable mTD;
		PageBGToLoad photoToLoad;

		public PageBGBitmapDisplayer(TransitionDrawable td, PageBGToLoad p) {
			mTD = td;
			photoToLoad = p;
		}

		public void run() {
			if (mTD != null) {
				ViewUtils.setBackground(photoToLoad.wysPage, mTD);
				mTD.startTransition(400);
			} else
				ViewUtils.setBackground(photoToLoad.wysPage, mStubDrawable);
		}
	}

	// ===============================================

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}
}
