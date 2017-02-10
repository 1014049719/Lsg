package com.talenton.lsg.ui.feed.video;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.R;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.server.bean.feed.MediaBean;
import com.talenton.lsg.ui.feed.image.AlbumBean;

import java.util.ArrayList;

public class AllVideoFragment extends BaseCompatFragment implements AdapterView.OnItemClickListener, View.OnClickListener{

	private GridView mGridView;
	private TextView mPreviewBtn;
	private Button mSendBtn;
	private ArrayList<MediaBean> mVideoDatas = new ArrayList<MediaBean>();
	//private List<PhotoPathBean> listPhotoPath;
	private AllVideoAdapter mVideosAdapter;
	private OnGetVideoDataListener mOnGetVideoDataListener;
	private OnAllVideoFragmentListener mOnAllVideoFragmentListener = null;
	
	public static AllVideoFragment getInstance(){
		AllVideoFragment fragment = new AllVideoFragment();
		return fragment;
	}
	
	public AllVideoFragment(){
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(getActivity() instanceof OnGetVideoDataListener){
			mOnGetVideoDataListener = (OnGetVideoDataListener) getActivity();
		}else{
			throw new IllegalArgumentException(
					activity + " must implement interface " + OnGetVideoDataListener.class.getSimpleName());
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_all_video_browser, container, false);
		//ReadDbPhotoPath();
		initView(view);
		initActionBar(view);
		//updateActionBarState();
		
		return view;
	}
	
	@Override
	protected int getTitleResourceId() {
		return R.string.video_upload;
	}

	@Override
	protected void onLeftClick() {
		int count = getFragmentManager().getBackStackEntryCount();
		if (count > 0) {
			getFragmentManager().popBackStack();
		} else {
			getActivity().finish();
		}
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.gv_chat_pic_send);
		mPreviewBtn = (TextView) view.findViewById(R.id.image_select_preview);
		mSendBtn = (Button) view.findViewById(R.id.btn_chat_pic_send);
		
		mGridView.setOnItemClickListener(this);
		mSendBtn.setOnClickListener(this);
		if (mPreviewBtn != null) {
			mPreviewBtn.setOnClickListener(this);
		}
		
		int margin = getResources().getDimensionPixelSize(R.dimen.space_5_0) * 4;
		int height = (int) ((getResources().getDisplayMetrics().widthPixels * 1.0f - margin) / 3);
		mVideosAdapter = new AllVideoAdapter();
		mVideosAdapter.setmHeight(height);
		mGridView.setAdapter(mVideosAdapter);
	}	
	
	private void updateActionBarState() {
		if (mPreviewBtn != null) {
			//mPreviewBtn.setText(getString(R.string.images_preview, mOnGetVideoDataListener.getSelected().size(),
			//		mOnGetVideoDataListener.getMaxNum()));

			mPreviewBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
		}
	}
	
	public void setAlbumBean(AlbumBean albumBean, int albumIndex){
		if (albumBean == null) {
			return;
		}
		if(albumIndex == 0){
			MediaBean camera = new MediaBean();
			camera.setImageId(-1);
			this.mVideoDatas = new ArrayList<MediaBean>();
			this.mVideoDatas.add(camera);
			this.mVideoDatas.addAll(albumBean.getImageBeans());
		}else{
			this.mVideoDatas = albumBean.getImageBeans();
		}
		this.mVideosAdapter.notifyDataSetChanged();
	}
	
	private void gotoPreviewSelectedVideo(){
		if(mOnAllVideoFragmentListener != null){
			mOnAllVideoFragmentListener.onClickPreviewBtn();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if (id == R.id.image_select_preview) {
			gotoPreviewSelectedVideo();
		}
		else if(id == R.id.btn_chat_pic_send){
			if(mOnAllVideoFragmentListener != null){
				mOnAllVideoFragmentListener.onClickNextStep();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		if(position == 0){
			mOnGetVideoDataListener.getSelected().clear();
			mOnAllVideoFragmentListener.onTakeVideo();
		}else if(position > 0){
			MediaBean ib = mVideoDatas.get(position);
			if(ib.isSelected()) return;
			mOnGetVideoDataListener.getSelected().clear();
			
			for(MediaBean mb : mVideoDatas){
				mb.setSelected(false);
			}
			ib.setSelected(true);
			mOnGetVideoDataListener.getSelected().add(ib);
			mVideosAdapter.notifyDataSetChanged();
			updateActionBarState();
		}
	}
	
	private class AllVideoAdapter extends BaseAdapter {
		
		private int mHeight;

		public void setmHeight(int mHeight) {
			this.mHeight = mHeight;
		}
		
		@Override
		public int getItemViewType(int position){
			MediaBean ib = (MediaBean) getItem(position);
			if (ib.getImageId() != -1) {
				return 1;
			} else {
				return 0;
			}
		}
		
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		
		@Override
		public int getCount() {
			return mVideoDatas == null ? 0 : mVideoDatas.size();
		}

		@Override
		public Object getItem(int i) {
			return mVideoDatas == null ? null : mVideoDatas.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}
		
		private View getCamera(View convertView, ViewGroup viewGroup) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_take_photo, viewGroup, false);
				convertView
						.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mHeight));
			}

			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			// TODO Auto-generated method stub
			int type = getItemViewType(position);
			if (type == 0) {
				View view = getCamera(convertView, viewGroup);
				View takePhoto = view.findViewById(R.id.take_photo);
				View takeVideo = view.findViewById(R.id.iv_take_video);
				if(takePhoto != null && takeVideo != null){
					takePhoto.setVisibility(View.GONE);
					takeVideo.setVisibility(View.VISIBLE);
				}
				return view;
			}
			
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_video_browser, viewGroup, false);
				convertView
						.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mHeight));
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.iv_pic_browser);
				holder.checkBox = (ImageView) convertView.findViewById(R.id.cb_pic_browser);
				//holder.cb_pic_already_select = (ImageView) convertView.findViewById(R.id.cb_pic_already_select);
				holder.tv_timer = (TextView) convertView.findViewById(R.id.tv_timer);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			MediaBean ib = (MediaBean) getItem(position);
			if (ib != null) {
				if (ib.getImageId() != -1) {
					int visible = ib.isSelected() ? View.VISIBLE : View.INVISIBLE;
					holder.checkBox.setVisibility(visible);
					//holder.setChecked(ib.isSelected());
					if(ib.getVideoTime() < 10)
						holder.tv_timer.setText(String.format("00:0%d", ib.getVideoTime()));
					else
						holder.tv_timer.setText(String.format("00:%d", ib.getVideoTime()));
					/*
					if (ReadAndCompareDbPhotoPath(ib.getThumbnailPath()) == 1) {
						holder.cb_pic_already_select.setVisibility(View.VISIBLE);
					} else {
						holder.cb_pic_already_select.setVisibility(View.GONE);
					}
					*/
					String path = ib.getThumbnailPath();
					if (!TextUtils.isEmpty(path) && !path.equals(holder.imageView.getTag())) {
						holder.imageView.setTag(path);
						int width = mHeight - convertView.getPaddingLeft() - convertView.getPaddingRight();
						int height = mHeight - convertView.getPaddingTop() - convertView.getPaddingBottom();
						holder.imageView.getLayoutParams().width = width;
						holder.imageView.getLayoutParams().height = height;

						ImageLoader.getInstance().displayImage("file://" + path, holder.imageView,
								ImageLoaderManager.DEFAULT_IMAGE_DISPLAYER_200);
					}
				}
			}else {
				holder.imageView.setTag(null);
			}
			return convertView;
		}
	}
	
	static class ViewHolder {
		ImageView imageView;
		ImageView checkBox;
		ImageView cb_pic_already_select;
		TextView tv_timer;
		boolean isCheck;
		/*
		public void setChecked(boolean isCheck) {
			this.isCheck = isCheck;
			
			if (isCheck) {
				checkBox.setImageResource(R.drawable.checkbox_album_img_selected);
			} else {
				checkBox.setImageResource(R.drawable.checkbox_album_img_unselected);
			}
			
		}
		*/
	}
	
	public interface OnAllVideoFragmentListener {
		public void onTakeVideo();
		public void onClickPreviewBtn();
		public void onClickNextStep();
	}
	
	public interface OnGetVideoDataListener{
		int getMaxNum();
		ArrayList<MediaBean> getSelected();
	}
	
	public void setOnAllVideoFragmentListener(OnAllVideoFragmentListener listener){
		mOnAllVideoFragmentListener = listener;
	}
}