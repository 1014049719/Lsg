package com.talenton.lsg.widget.dialog;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.R;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.server.bean.user.BookIntro;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BookIntrDialog extends DialogFragment {
	private ListView mIntroList;
	private View mRoot;
	private IntroAdapter mAdapter;

	public static BookIntrDialog newInstance(ArrayList<BookIntro> intros) {
		if (intros == null || intros.size() == 0) {
			return null;
		}
		BookIntrDialog fragment = new BookIntrDialog();
		Bundle bundle = new Bundle();
		bundle.putSerializable("key_book_intros", intros);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		ArrayList<BookIntro> intros = null;
		if (bundle != null && bundle.containsKey("key_book_intros")) {
			intros = (ArrayList<BookIntro>) getArguments().getSerializable("key_book_intros");
		} else {
			dismiss();
		}
		Dialog dialog = new Dialog(getActivity(), R.style.common_dialog);
		dialog.setContentView(R.layout.dialog_book_intro);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		mIntroList = (ListView) dialog.findViewById(R.id.list);
		mAdapter = new IntroAdapter(getActivity(), intros);
		mIntroList.setAdapter(mAdapter);

		mRoot = (LinearLayout) dialog.findViewById(R.id.root);
		mRoot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		mIntroList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				dismiss();
				
			}
			
		});

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		return dialog;
	}

	private class IntroAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<BookIntro> mBBs;

		public IntroAdapter(Context context, ArrayList<BookIntro> intros) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mBBs = intros;
		}

		public int getCount() {
			return mBBs == null ? 0 : mBBs.size();
		}

		public BookIntro getItem(int position) {
			return mBBs == null ? null : mBBs.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_gift_book_intro, parent, false);
				holder.logo = (ImageView) convertView.findViewById(R.id.logo);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			BookIntro item = getItem(position);
			ImageLoader.getInstance().displayImage(item.url, holder.logo,
					ImageLoaderManager.DEFAULT_IMAGE_DISPLAYER_200);
			holder.title.setText(item.title);
			holder.desc.setText(item.desc);
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView logo;
		TextView title;
		TextView desc;
	}

}