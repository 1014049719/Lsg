package com.talenton.lsg.ui.feed.image;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.R;

import java.util.ArrayList;

public class AlbumFragment extends BaseCompatFragment implements AdapterView.OnItemClickListener {

	private static final String TAG = "pic";

	private ListView mListView;

	private ArrayList<AlbumBean> mAlbumDatas = new ArrayList<AlbumBean>();

	private AlbumAdapter mAlbumAdapter = null;

	private OnAlbumItemClickListener mOnAlbumItemClickListener = null;

	public static AlbumFragment getInstance(Bundle bundle) {
		AlbumFragment albumFragment = new AlbumFragment();
		albumFragment.setArguments(bundle);
		return albumFragment;
	}

	public AlbumFragment() {
	}

	public void setOnAlbumItemClickListener(OnAlbumItemClickListener mOnAlbumItemClickListener) {
		this.mOnAlbumItemClickListener = mOnAlbumItemClickListener;
	}

	public void setAlbumDatas(ArrayList<AlbumBean> mAlbumDatas) {
		this.mAlbumDatas = mAlbumDatas;
		if (mAlbumAdapter != null) {
			mAlbumAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (getActivity() == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.fragment_pic_album, container, false);
		mListView = (ListView) view.findViewById(R.id.lv_chat_album);
		mListView.setOnItemClickListener(this);
		initActionBar(view);
		mAlbumAdapter = new AlbumAdapter(getActivity());
		mListView.setAdapter(mAlbumAdapter);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
		if (mOnAlbumItemClickListener != null) {
			mOnAlbumItemClickListener.onAlbumItemClick(position);
		}
	}

	@Override
	protected int getTitleResourceId() {
		// TODO Auto-generated method stub
		return R.string.select_album;
	}

	@Override
	protected void onLeftClick() {
		getFragmentManager().popBackStack();
	}

	class AlbumAdapter extends BaseAdapter {

		private Context mContext;

		public AlbumAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return mAlbumDatas == null ? 0 : mAlbumDatas.size();
		}

		@Override
		public Object getItem(int i) {
			return mAlbumDatas == null ? null : mAlbumDatas.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {

			final ViewHolder holder;

			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_album, viewGroup, false);

				holder = new ViewHolder();
				holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_chat_album_first);
				holder.mCountTv = (TextView) convertView.findViewById(R.id.tv_chat_album_count);
				holder.mTitleTv = (TextView) convertView.findViewById(R.id.tv_chat_album_title);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			AlbumBean ab = (AlbumBean) getItem(position);

			if (ab != null) {

				int wh = mContext.getResources().getDimensionPixelSize(R.dimen.width_60);
				holder.mCountTv.setText(mContext.getString(R.string.image_album_count, ab.getImageBeans().size()));
				holder.mTitleTv.setText(ab.getAlbumName());
				holder.mImageView.getLayoutParams().width = wh;
				holder.mImageView.getLayoutParams().height = wh;
				ImageLoader.getInstance().displayImage("file://" + ab.getFirstImagePath(), holder.mImageView);
				// LocalImageLoader.getInstance().loadImage(
				// ab.getFirstImagePath(), wh, wh,
				// new LocalImageLoader.NativeImageCallBack() {
				// @Override
				// public void onImageLoader(Bitmap bitmap, String path) {
				// holder.mImageView.setImageBitmap(bitmap);
				//
				// }
				// }, true);

			}

			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView mImageView;

		public TextView mTitleTv;
		public TextView mCountTv;

	}

	public interface OnAlbumItemClickListener {
		public void onAlbumItemClick(int position);
	}

}