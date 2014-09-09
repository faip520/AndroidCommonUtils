package com.A1w0n.androidcommonutils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.A1w0n.androidcommonutils.debugutils.Logger;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * 通过读取文件 /dev/graphics/fb0获取屏幕截图
 */
public class ScreenShotUtils {
	
	private static ScreenShotUtils mInstance;
	
	// Android下FrameBuffer，一般在这里
	private final static String PATH_FB0 = "/dev/graphics/fb0";
	
	private static File mFb0File;
	private static FileInputStream graphics = null;
	private static byte[] Fb0Bytes;
	
	private ScreenShotUtils () {
		mFb0File = new File(PATH_FB0);
		if (!mFb0File.exists()) {
			throw new RuntimeException("/dev/graphics/fb0 does not exist!");
		}
		
		chmodFb0File();
		Fb0Bytes = new byte[5257987]; // 像素
	}
	
	private void chmodFb0File() {
		// 初始化事件文件的权限
		Process sh = null;
		try {
			sh = Runtime.getRuntime().exec("su", null, null);
			OutputStream os = sh.getOutputStream();
			os.write(("chmod 777 " + mFb0File.getAbsolutePath()).getBytes());
			os.flush();
			os.close();
			sh.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sh != null) {
				sh.destroy();
			}
		}
	}
	
	public static ScreenShotUtils getInstance() {
		if (mInstance == null) {
			mInstance = new ScreenShotUtils();
		}
		
		return mInstance;
	}
	
	/**
	 * 测试截图
	 */
	@SuppressLint("SdCardPath")
	public void takeScreenShot(File target) {
		if (target == null || !target.exists() || !target.canWrite()) {
			Logger.e("Target file unreachable or unwritable!");
		}
		
		target.delete();
		
		long start = System.currentTimeMillis();
		try {
			Bitmap bm = getScreenBitmap();
			saveMyBitmap(bm, target);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		Log.i("Screenshot", "time cost:" + (end - start));
	}

	/**
	 * 保存bitmap到文件
	 * 
	 * @param bitmap
	 * @param bitName
	 * @throws IOException
	 */
	private void saveMyBitmap(Bitmap bitmap, File targetFile)
			throws IOException {
		FileOutputStream fOut = new FileOutputStream(targetFile);

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		fOut.flush();
		fOut.close();
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private synchronized Bitmap getScreenBitmap() throws IOException {
		try {
			graphics = new FileInputStream(mFb0File);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		DataInputStream dStream = new DataInputStream(graphics);
		
		long stat = System.currentTimeMillis();
		// 把fb0读进字节数组
		dStream.readFully(Fb0Bytes);
		dStream.close();
		Logger.d("Read time =  " + (System.currentTimeMillis() - stat));

		int line_length = 7680;
		int w = 1217;
		int widthBytes = 1217 * 4;
		int h = 685;
		
		int[] pixels = new int[5257987];
		int row;
		int offset = 0;
		
		long stat2 = System.currentTimeMillis();
		for (int i = 0; i < pixels.length; i+=4) {
			row = i / line_length;
			
			if (row >= h) break;
			if ((i - row * line_length) >= widthBytes) continue;
			// fb0里面存储的BGRA中的A都是FF
			// byte类型左移后是不变的，必须和0xFF变成int类型后，左移才有效
			pixels[offset] =  (0xFF << 24) | ((Fb0Bytes[i + 2]& 0xFF) << 16) | ((Fb0Bytes[i + 1]& 0xFF) << 8) | (Fb0Bytes[i]& 0xFF);
			offset++;
		}
		Logger.d("Moce time =  " + (System.currentTimeMillis() - stat2));
		
		return Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888);
	}
}