package com.A1w0n.androidcommonutils;

import android.os.Parcel;
import android.os.Parcelable;

public class CommonParcelableBean implements Parcelable {
	public String user_id;
	public String content;
	public String create_time;

	public CommonParcelableBean() {
		
	}

	public CommonParcelableBean(Parcel parcel) {
		String[] infos = new String[3];
		parcel.readStringArray(infos);
		this.user_id = infos[0];
		this.create_time = infos[1];
		this.content = infos[2];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { user_id, create_time, content });
	}

	public static final Parcelable.Creator<CommonParcelableBean> CREATOR = new Parcelable.Creator<CommonParcelableBean>() {
		public CommonParcelableBean createFromParcel(Parcel in) {
			return new CommonParcelableBean(in);
		}

		public CommonParcelableBean[] newArray(int size) {
			return new CommonParcelableBean[size];
		}
	};
}
