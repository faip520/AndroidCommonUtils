package com.A1w0n.androidcommonutils.PackageUtils;

import java.io.Serializable;

public class App implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public String macAddress;
	public String userName;
	/**
	 * 缩略图路径
	 */
	public String thumbnailPath;
	/**
	 * 路径名字
	 */
	public String apkPath;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 包名
	 */
	public String packName;
	/**
	 * 版本号
	 */
	public int versionCode;
	/**
	 * 版本名字
	 */
	public String versionName;
	/**
	 * 文件大小
	 */
	public long fileSize = 0;
	/**
	 * 页面编辑时是否为选中状态
	 */
	public boolean isSelect = false;
	/**
	 * 缩略图
	 */
	public byte[] head;
	/**
	 * 上传文件ID
	 */
	public long fileID;
	/**
	 * 是否已经上传成功
	 */
	public boolean isUpload = false;
	public long uploadProcessor = 0;
	public boolean isDown = false;
	public long downProcessor = 0;
}
