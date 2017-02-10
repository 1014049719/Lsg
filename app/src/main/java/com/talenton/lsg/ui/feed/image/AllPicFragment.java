package com.talenton.lsg.ui.feed.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.R;
import com.talenton.lsg.base.dao.model.PhotoPathBean;
import com.talenton.lsg.base.server.DBHelper;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.server.bean.feed.MediaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/11/21.
 */
public class AllPicFragment extends BaseCompatFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

	private static final String TAG = "pic";
	public static final String SUFFIX = "tmp";

	private GridView mGridView;
	private TextView mPreviewBtn, mRangeBtn, mSkipBtn;
	private Button mSendBtn;
	private View  mSelectAll;
	private ImageView mAlbumIv, mCheckSelectAll;
	private AlbumBean mAlbumBean;
	private boolean mIsSelectAll = false;

	private ArrayList<MediaBean> mPicDatas = new ArrayList<MediaBean>();
	// private ArrayList<ImageBean> mSelectedDatas = new ArrayList<ImageBean>();

	private AllPicsAdapter mPicsAdapter;

	private OnGetPicDataListener mOnGetPicDataListener;
	private OnAllPicFragmentListener mOnAllPicFragmentListener = null;

	private int mAlbumIndex;

	private OnSendBtnClickListener mOnSendBtnClickListener = null;

	private List<PhotoPathBean> listPhotoPath;

	public void setOnSendBtnClickListener(OnSendBtnClickListener mOnSendBtnClickListener) {
		this.mOnSendBtnClickListener = mOnSendBtnClickListener;
	}

	public static AllPicFragment getInstance() {
		AllPicFragment fragment = new AllPicFragment();
		return fragment;
	}

	public AllPicFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (getActivity() instanceof OnGetPicDataListener) {
			mOnGetPicDataListener = (OnGetPicDataListener) getActivity();
		} else {
			throw new IllegalArgumentException(
					activity + " must implement interface " + OnGetPicDataListener.class.getSimpleName());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		int resId = R.layout.fragment_all_pic_browser;
		if (mOnGetPicDataListener.getFrom() == AllPicBrowserActivity.FROM_GUIDE_GALLERY) {
			resId = R.layout.fragment_all_pic_guide_browser;
		}
		View view = inflater.inflate(resId, container, false);
		ReadDbPhotoPath();
		initView(view);
		if (resId == R.layout.fragment_all_pic_browser) {
			initActionBar(view);
			if (mAlbumBean != null && mActionBarTitle != null) {
				mActionBarTitle.setText(mAlbumBean.getAlbumName());
			}
		}

		updateActionBarState();
		initActionBar(view);
		return view;
	}

	public void ReadDbPhotoPath() {

		listPhotoPath = DBHelper.getInstance().list("", DBHelper.DAO_PHOTO);
	}

	public int ReadAndCompareDbPhotoPath(String sReadDbPhotoPath) {

		if (sReadDbPhotoPath == null) {
			return 0;
		}
		for (int i = 0; i < listPhotoPath.size(); i++) {

			if (sReadDbPhotoPath.equals(listPhotoPath.get(i).getPhotoPath())) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void setOnClickAlbumBtnListener(OnAllPicFragmentListener mOnClickAlbumBtnListener) {
		this.mOnAllPicFragmentListener = mOnClickAlbumBtnListener;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
		if (mOnGetPicDataListener.getFrom() == AllPicBrowserActivity.FROM_PHOTO && position == 0 && mAlbumIndex == 0) {
			if (mOnGetPicDataListener.getSelected().size() >= mOnGetPicDataListener.getMaxNum()
					&& getActivity() != null) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.image_beyong_limit, mOnGetPicDataListener.getMaxNum()),
						Toast.LENGTH_SHORT).show();
			} else {
				mOnAllPicFragmentListener.onTakePicture();
			}
			return;
		}
		if (position >= 0) {
			ViewHolder holder = (ViewHolder) view.getTag();

			boolean checked = holder.isCheck;
			if (!checked) {
				if (mOnGetPicDataListener.getSelected().size() >= mOnGetPicDataListener.getMaxNum()
						&& getActivity() != null) {
					Toast.makeText(getActivity(),
							getResources().getString(R.string.image_beyong_limit, mOnGetPicDataListener.getMaxNum()),
							Toast.LENGTH_SHORT).show();
					return;
				}

				holder.setChecked(true);
				MediaBean ib = mPicDatas.get(position);
				ib.setSelected(true);
				mOnGetPicDataListener.getSelected().add(ib);
			} else {
				holder.setChecked(false);
				MediaBean ib = mPicDatas.get(position);
				ib.setSelected(false);
				mOnGetPicDataListener.getSelected().remove(ib);
			}
			updateActionBarState();
		}
	}

	@Override
	public void onClick(View view) {

		int id = view.getId();
		if (id == R.id.image_select_preview) {
			gotoPreviewSelectedPic();
		}
		if (id == R.id.btn_chat_pic_send) {
			if (mOnSendBtnClickListener != null) {
				mOnSendBtnClickListener.onClickSendBtn(mOnGetPicDataListener.isUseOriginal());
			}
		}
		if (id == R.id.image_select_all) {
			mIsSelectAll = !mIsSelectAll;
			if (mIsSelectAll) {
				mCheckSelectAll.setImageResource(R.mipmap.daily_report_checkbox_select);
				ArrayList<MediaBean> selected = mOnGetPicDataListener.getSelected();
				int toSelect = mOnGetPicDataListener.getMaxNum() - selected.size();
				if (toSelect <= 0) {
					return;
				}
				for (MediaBean ib : mPicDatas) {
					if (!ib.isSelected()) {
						ib.setSelected(true);
						selected.add(ib);
						--toSelect;
						if (toSelect <= 0) {
							mPicsAdapter.notifyDataSetChanged();
							updateActionBarState();
							return;
						}
					}
				}
			} else {
				mCheckSelectAll.setImageResource(R.mipmap.daily_report_checkbox);
				ArrayList<MediaBean> selected = mOnGetPicDataListener.getSelected();
				int toRemove = selected.size();
				if (toRemove <= 0) {
					return;
				}
				for (MediaBean ib : mPicDatas) {
					if (ib.isSelected()) {
						ib.setSelected(false);
						selected.remove(ib);
						--toRemove;
						if (toRemove <= 0) {
							mPicsAdapter.notifyDataSetChanged();
							updateActionBarState();
							return;
						}
					}
				}
			}
		}

		if(id==R.id.skip){
			getActivity().finish();
			getActivity().overridePendingTransition(0, 0);
		}

	}

	public void setAlbumBean(AlbumBean albumBean, int albumIndex) {
		if (albumBean == null) {
			return;
		}
		this.mAlbumIndex = albumIndex;
		this.mAlbumBean = albumBean;

		if (mActionBarTitle != null) {
			mActionBarTitle.setText(albumBean.getAlbumName());
		}
		if (albumIndex == 0 && mOnGetPicDataListener.getFrom() == AllPicBrowserActivity.FROM_PHOTO) {
			MediaBean camera = new MediaBean();
			camera.setImageId(-1);
			this.mPicDatas = new ArrayList<MediaBean>();
			this.mPicDatas.add(camera);
			this.mPicDatas.addAll(albumBean.getImageBeans());
		} else {
			this.mPicDatas = albumBean.getImageBeans();
		}
		mPicsAdapter.notifyDataSetChanged();

	}

	private void initView(View view) {

		mGridView = (GridView) view.findViewById(R.id.gv_chat_pic_send);
		mPreviewBtn = (TextView) view.findViewById(R.id.image_select_preview);
		mRangeBtn = (TextView) view.findViewById(R.id.image_select_range);
		mSendBtn = (Button) view.findViewById(R.id.btn_chat_pic_send);
		mSkipBtn = (TextView) view.findViewById(R.id.skip);

		mGridView.setOnItemClickListener(this);
		mSendBtn.setOnClickListener(this);
		if (mPreviewBtn != null) {
			mPreviewBtn.setOnClickListener(this);
		}
		if (mSkipBtn != null) {
			mSelectAll = view.findViewById(R.id.image_select_all);
			mCheckSelectAll = (ImageView) view.findViewById(R.id.cb_image_select_all);
			mSkipBtn.setOnClickListener(this);
			mSelectAll.setOnClickListener(this);
		}

		int margin = getResources().getDimensionPixelSize(R.dimen.space_5_0) * 4;
		int height = (int) ((getResources().getDisplayMetrics().widthPixels * 1.0f - margin) / 3);
		mPicsAdapter = new AllPicsAdapter();
		mPicsAdapter.setmHeight(height);
		mGridView.setAdapter(mPicsAdapter);

		if (mOnGetPicDataListener.getFrom() == AllPicBrowserActivity.FROM_GALLERY) {
			mSendBtn.setText(R.string.post_action);
		} else if (mOnGetPicDataListener.getFrom() != AllPicBrowserActivity.FROM_GUIDE_GALLERY) {
			mSendBtn.setText(R.string.image_send);
		}

	}

	@Override
	protected void onRightClick(MenuItem item) {
		gotoImageAlbumFragment();
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

	@Override
	protected int getMenuResourceId() {
		return R.menu.menu_select_photo;
	}

	private void gotoImageAlbumFragment() {
		if (mOnAllPicFragmentListener != null) {
			mOnAllPicFragmentListener.onClickAlbumBtn();
		}
	}

	private void gotoPreviewSelectedPic() {
		if (mOnAllPicFragmentListener != null) {
			mOnAllPicFragmentListener.onClickPreviewBtn();
		}
	}

	private void updateActionBarState() {
		if (mPreviewBtn != null) {
			mPreviewBtn.setText(getString(R.string.images_preview, mOnGetPicDataListener.getSelected().size(),
					mOnGetPicDataListener.getMaxNum()));
		} else if (mRangeBtn != null) {
			mRangeBtn.setText(getString(R.string.images_select_range, mOnGetPicDataListener.getSelected().size(),
					mOnGetPicDataListener.getMaxNum()));
		}
	}

	private class AllPicsAdapter extends BaseAdapter {

		private int mHeight;

		public void setmHeight(int mHeight) {
			this.mHeight = mHeight;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
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
			return mPicDatas == null ? 0 : mPicDatas.size();
		}

		@Override
		public Object getItem(int i) {
			return mPicDatas == null ? null : mPicDatas.get(i);
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

			int type = getItemViewType(position);
			if (type == 0) {
				return getCamera(convertView, viewGroup);
			}

			final ViewHolder holder;

			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_pic_browser, viewGroup, false);
				convertView
						.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mHeight));
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.iv_pic_browser);
				holder.checkBox = (ImageView) convertView.findViewById(R.id.cb_pic_browser);

				holder.cb_pic_already_select = (ImageView) convertView.findViewById(R.id.cb_pic_already_select);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			MediaBean ib = (MediaBean) getItem(position);
			if (ib != null) {
				if (ib.getImageId() != -1) {
					holder.checkBox.setVisibility(View.VISIBLE);
					holder.setChecked(ib.isSelected());
					String path = TextUtils.isEmpty(ib.getThumbnailPath()) ? ib.getPath() : ib.getThumbnailPath();

					if (ReadAndCompareDbPhotoPath(ib.getPath()) == 1) {
						holder.cb_pic_already_select.setVisibility(View.VISIBLE);
					} else {
						holder.cb_pic_already_select.setVisibility(View.GONE);
					}

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

			} else {
				holder.imageView.setTag(null);
			}

			return convertView;
		}

	}

	static class ViewHolder {
		ImageView imageView;
		ImageView checkBox;
		ImageView cb_pic_already_select;
		boolean isCheck;

		public void setChecked(boolean isCheck) {
			this.isCheck = isCheck;
			if (isCheck) {
				checkBox.setImageResource(R.mipmap.checkbox_album_img_selected);
			} else {
				checkBox.setImageResource(R.mipmap.checkbox_album_img_unselected);
			}
		}

	}

	public interface OnAllPicFragmentListener {
		public void onTakePicture();

		public void onClickAlbumBtn();

		public void onPicItemClick(View view, int albumPosition, int position);

		public void onClickPreviewBtn();

	}

	public interface OnSendBtnClickListener {
		public void onClickSendBtn(boolean isUseOriginal);
	}

	public interface OnGetPicDataListener {
		int getFrom();

		int getMaxNum();

		ArrayList<MediaBean> getSelected();

		boolean isUseOriginal();

		void setUseOriginal(boolean orginal);
	}

}