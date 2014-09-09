package com.A1w0n.androidcommonutils.ParcelableUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.A1w0n.androidcommonutils.debugutils.Logger;

import android.util.Log;

public class SerializeUtil {

	/**
	 * 将对象序列化成byte[]
	 * 
	 * @param object
	 *            需要序列化的对象
	 * @return
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			baos.close();
			oos.close();
			return bytes;
		} catch (Exception e) {
			Logger.d("对象序列化失败。");
			return null;
		}
	}
	
	/**
	 * 将对象序列化成String
	 * 
	 * @param object
	 *            需要序列化的对象
	 * @return
	 */
	public static String serializeToString(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			baos.close();
			oos.close();
			return baos.toString("utf-8");
		} catch (Exception e) {
			Logger.d("对象序列化失败。");
			return null;
		}
	}
	
	/**
	 * 将byte[]反序列化成对象
	 * 
	 * @param bytes
	 *            需要凡序列化的byte[]
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object obj = ois.readObject();
			bais.close();
			ois.close();
			return obj;
		} catch (Exception e) {
			Logger.w(Log.getStackTraceString(e));
			Logger.d("对象反序列化失败。");
			return null;
		}
	}
}
