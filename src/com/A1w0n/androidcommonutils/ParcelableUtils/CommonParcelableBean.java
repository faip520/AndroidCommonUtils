package com.A1w0n.androidcommonutils.ParcelableUtils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *	通用Parcelable类实现，其他类要实现Parcelable可以参考这个实现
 */
public class CommonParcelableBean implements Parcelable {
    private String uid;
    private String nickname;
    private String remark;

	public CommonParcelableBean() {
		
	}

	/**
	 * 必须实现一个参数只有Parcel的构造函数，在里面读取信息到自己的成员
	 * 变量
	 */
	public CommonParcelableBean(Parcel parcel) {
		// 按照你存入的顺序读取就好了哦
        uid = parcel.readString();
        nickname = parcel.readString();
        remark = parcel.readString();
	}

	/**
	 * 这个不能没有，返回0就可以了
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 按顺序把成员变量写入Parcel，读取的时候按照写入的顺序读取就OK了
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(nickname);
        dest.writeString(remark);
	}

	/**
	 * 这一段如果要修改，只要修改类名字就好了
	 */
	public static final Parcelable.Creator<CommonParcelableBean> CREATOR = new Parcelable.Creator<CommonParcelableBean>() {
		public CommonParcelableBean createFromParcel(Parcel in) {
			return new CommonParcelableBean(in);
		}

		public CommonParcelableBean[] newArray(int size) {
			return new CommonParcelableBean[size];
		}
	};
}
