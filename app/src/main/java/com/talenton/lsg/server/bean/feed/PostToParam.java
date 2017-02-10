package com.talenton.lsg.server.bean.feed;

import android.os.Parcel;
import android.os.Parcelable;


public class PostToParam implements Parcelable {

	public final static int POST_IMAGE = 0;// 图片类型
	public final static int POST_VIDEO = 1;// 视频类型
	public final static int POST_GALLERY = 3;// 批量导入类型
	public final static int POST_TEXT = 4;// 文本类型
	public final static int POST_HELP = 5;// 求助
	public long circleId;
	public int circleType;
	public int attentionType;
	public int mediaType,ispajs;
	public String name;
	public long tid; //话题ID
	public String guid;//话题的GUID
	public int count;
	public int height;
	public long commentUID;
	public long uid;

	public PostToParam(long circleId, int circleType, long tid, String guid, String name) {
		super();
		this.tid = tid;
		this.circleId = circleId;
		this.circleType = circleType;
		this.guid = guid;
		this.name = name;
	}

	public PostToParam(int circleType,  long circleId, String name) {
		this.tid = 0;
		this.circleType = circleType;
		this.circleId = circleId;
		this.name = name;
	}

	public PostToParam(){

	}

	public PostToParam clone(){
		PostToParam param = new PostToParam();
		param.name = name;
		param.circleId = circleId;
		param.circleType = circleType;
		param.attentionType = attentionType;
		param.mediaType = mediaType;
		param.ispajs = ispajs;
		param.tid = tid;
		param.guid = guid;
		param.count = count;
		param.height = height;
		param.commentUID = commentUID;
		param.uid = uid;
		return param;
	}

	public void setPostToParam(Feeds feeds) {
		mediaType = MediaBean.TYPE_PIC;
		if (feeds.attachinfo != null && feeds.attachinfo.size() > 0) {
			mediaType = feeds.attachinfo.get(0).itype;
		}
		if (mediaType != MediaBean.TYPE_PIC && mediaType != MediaBean.TYPE_VIDEO) {
			mediaType = MediaBean.TYPE_PIC;
		}
		this.name = feeds.circle_name;
		this.circleId = feeds.circle_id;
		if(feeds.ext_circle != null)
			this.circleType = feeds.ext_circle.circle_type;
		this.attentionType = feeds.ext_circle_member_attention_type;
		this.tid = feeds.tid;
		this.guid = feeds.guid;
	}

	public void setIsPajs(int ispajs){
		this.ispajs = ispajs;
	}

	public boolean isValid() {
		return true;

		//return circleType == CircleListData.CIRCLE_TYPE_BABY;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(name);
		dest.writeLong(circleId);
		dest.writeInt(circleType);
		dest.writeInt(attentionType);
		dest.writeInt(mediaType);
		dest.writeInt(ispajs);
		dest.writeLong(tid);
		dest.writeString(guid);
		dest.writeInt(count);
		dest.writeInt(height);
		dest.writeLong(commentUID);
		dest.writeLong(uid);

	}

	public static Creator<PostToParam> CREATOR = new Creator<PostToParam>() {

		@Override
		public PostToParam createFromParcel(Parcel source) {
			PostToParam param = new PostToParam();
			param.name = source.readString();
			param.circleId = source.readLong();
			param.circleType = source.readInt();
			param.attentionType = source.readInt();
			param.mediaType = source.readInt();
			param.ispajs = source.readInt();
			param.tid = source.readLong();
			param.guid = source.readString();
			param.count = source.readInt();
			param.height = source.readInt();
			param.commentUID = source.readLong();
			param.uid = source.readLong();
			return param;
		}

		@Override
		public PostToParam[] newArray(int size) {
			return new PostToParam[size];
		}

	};
}