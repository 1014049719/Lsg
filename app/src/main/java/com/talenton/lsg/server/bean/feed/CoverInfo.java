package com.talenton.lsg.server.bean.feed;

import android.os.Parcel;
import android.os.Parcelable;

public final class CoverInfo implements Parcelable {

	public String filepath;
	public String orgpath;
	public int height;
	public int width;
	public int itype;
	public int remote;// 0:本地 1:服务器 2:七牛
	public int size;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// TODO Auto-generated method stub
		parcel.writeInt(itype);
		parcel.writeInt(remote);
		parcel.writeString(orgpath);
		parcel.writeString(filepath);
		parcel.writeInt(height);
		parcel.writeInt(width);
		parcel.writeInt(size);
	}
	
	public static final Parcelable.Creator<CoverInfo> CREATOR = new Parcelable.Creator<CoverInfo>(){

		@Override
		public CoverInfo createFromParcel(Parcel parcel) {
			// TODO Auto-generated method stub
			CoverInfo cover = new CoverInfo();
			cover.itype = parcel.readInt();
			cover.remote = parcel.readInt();
			cover.orgpath = parcel.readString();
			cover.filepath = parcel.readString();
			cover.height = parcel.readInt();
			cover.width = parcel.readInt();
			cover.size = parcel.readInt();
			return cover;
		}

		@Override
		public CoverInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
}