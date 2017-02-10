package com.talenton.lsg.ui.feed.video;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.util.FileUtil;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.server.bean.feed.MediaBean;
import com.talenton.lsg.ui.feed.image.AlbumBean;
import com.talenton.lsg.ui.feed.image.AllPicBrowserActivity;
import com.talenton.lsg.ui.school.PlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllVideoBrowserActivity extends BaseCompatActivity implements
		AllVideoFragment.OnGetVideoDataListener, AllVideoFragment.OnAllVideoFragmentListener{
	
	private static final int REQUEST_VIDEO = 2001;
	private static final int REQUEST_START_VIDEO = 2002;
	
	private AllVideoFragment mAllVideoFragment;
	private ArrayList<MediaBean> mVideoDatas = new ArrayList<MediaBean>();
	public ArrayList<MediaBean> mSelectedDatas = new ArrayList<MediaBean>();
	
	public static void startAllVideoBrowserActivity(Context context){
		Intent intent = new Intent(context, AllVideoBrowserActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_browser);
		mSelectedDatas = new ArrayList<MediaBean>();
		mAllVideoFragment = AllVideoFragment.getInstance();
		mAllVideoFragment.setOnAllVideoFragmentListener(this);
		getSupportFragmentManager().beginTransaction().add(R.id.fl_pic_browser_content, mAllVideoFragment).commit();
		loadVideos();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(data != null){
				if(requestCode == REQUEST_VIDEO || requestCode == REQUEST_START_VIDEO){
					String imagevideo = data.getStringExtra("imagevideo");
					String video = data.getStringExtra("video");
					String timer = data.getStringExtra("timer");
					File videofile = new File(video);
					if(!videofile.exists()){
						return;
					}
					int second = 0;
					try{
						second = Integer.parseInt(timer);
					}catch(Exception e){
						second = 0;
					}
					Intent intent = new Intent();
					ArrayList<MediaBean> res = new ArrayList<MediaBean>();
					MediaBean camera = new MediaBean(MediaBean.TYPE_VIDEO);
					camera.remote = MediaBean.ADDR_LOCAL;
					camera.setImageId(1);
					camera.setModified(videofile.lastModified());
					camera.setVideoTime(second);
					long size = videofile.length() / 1024;
					camera.setSize((int)size);
					camera.orgpath = video;
					camera.filepath = video;
					camera.setThumbnailPath(imagevideo);
					camera.setCover(imagevideo);
					camera.setSelected(true);
					res.add(camera);
					intent.putParcelableArrayListExtra(AllPicBrowserActivity.KEY_SELECTED_LIST_PATH, res);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		}else{
			if(requestCode == REQUEST_START_VIDEO){
				finish();
			}
		}
	}
	
	private void loadVideos() {
		// TODO Auto-generated method stub
		QueryVideoTask task = new QueryVideoTask();
		task.execute();
	}

	@Override
	public int getMaxNum() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public ArrayList<MediaBean> getSelected() {
		// TODO Auto-generated method stub
		return mSelectedDatas;
	}

	@Override
	public void onTakeVideo() {
		// TODO Auto-generated method stub
		if (!checkPermission(Manifest.permission.CAMERA
		,Manifest.permission.RECORD_AUDIO
		,Manifest.permission.READ_EXTERNAL_STORAGE
		,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
			return;
		}
		Intent intent = new Intent(this, RecorderVideoActivity.class);
		startActivityForResult(intent, REQUEST_VIDEO);
	}

	@Override
	public void onPermissionsResult(Map<String, Boolean> permissions){
		if (permissions == null || permissions.size() == 0){
			return;
		}
		Boolean object = permissions.get(Manifest.permission.CAMERA);
		if(object != null && !object) {
			XLTToast.makeText(this, R.string.permission_camera_info, Toast.LENGTH_LONG).show();
			return;
		}
		int i = 0;
		for (Boolean b : permissions.values()){
			if(b== null || !b) break;
			i++;
		}
		if (i != permissions.size()){
			XLTToast.makeText(this, R.string.permission_storage_info, Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(this, RecorderVideoActivity.class);
		startActivityForResult(intent, REQUEST_VIDEO);
	}

	@Override
	public void onClickPreviewBtn() {
		// TODO Auto-generated method stub
		ArrayList<MediaBean> selects = getSelected();
		if(selects == null || selects.isEmpty()){
			return;
		}
		PlayerActivity.startPlayerActivity(this, selects.get(0).getVideoUrl());
		//VideoPlayerActivity.startVideoPlayerActivity(this, selects.get(0));
	}
	
	@Override
	public void onClickNextStep(){
		ArrayList<MediaBean> selects = getSelected();
		if(selects == null || selects.isEmpty()){
			XLTToast.makeText(this, "至少选中一个视频哦").show();
			return;
		}
		for(MediaBean mb : selects){
			mb.setCover(mb.getThumbnailPath());
		}
		Intent intent = new Intent();
		intent.putParcelableArrayListExtra(AllPicBrowserActivity.KEY_SELECTED_LIST_PATH, selects);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	
	class QueryVideoTask extends AsyncTask<Void, Void, List<MediaBean>> {
		
		HashMap<String, String> mimeTypes = new HashMap<String, String>(){
			{
				//put(".3gp", "video");
				//put(".avi", "video");
				//put(".mpg4", "video");
				put(".jpg", "image");
				put(".jpeg", "image");
			}
		};
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected List<MediaBean> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<MediaBean> datas = new ArrayList<MediaBean>();
			HashMap<String, Integer> names = new HashMap<String, Integer>();
			
			
			File sdPath = FileUtil.getExternalPath(AllVideoBrowserActivity.this, FileUtil.VIDEO_RECORD);
			if(sdPath != null && sdPath.listFiles() != null && sdPath.listFiles().length > 0){
				for(File file : sdPath.listFiles()){
					String name = file.getName();
					int dotIndex = name.lastIndexOf(".");
					if(dotIndex < 0) continue;
					
					String end = name.substring(dotIndex, name.length());
					if(!mimeTypes.containsKey(end)) continue;
					if(names.containsKey(name)) continue;
					names.put(name, 1);
					int vIndex = name.indexOf("v");
					int second = 0;
					if(vIndex == -1){
						name = name.substring(0, dotIndex);
					}else{
						try{
							second = Integer.parseInt(name.substring(vIndex+1, dotIndex));
						}catch(Exception e){
							second = 0;
						}
						name = name.substring(0, vIndex);
					}
					
					String videoPath = String.format("%s/%s.mp4", sdPath.getAbsolutePath(), name);
					File videoFile = new File(videoPath);
					if(!videoFile.exists()) continue;
					if(videoFile.length() > 10.f * 1024.f * 1024.f) continue;
					
					MediaBean ib = new MediaBean(MediaBean.TYPE_VIDEO);
					ib.remote = MediaBean.ADDR_LOCAL;
					ib.setImageId(1);
					ib.setModified(file.lastModified());
					ib.setVideoTime(second);
					long size = videoFile.length() / 1024;
					ib.setSize((int)size);
					ib.orgpath = videoPath;
					ib.filepath = videoPath;
					ib.setThumbnailPath(String.format("%s/%s", sdPath.getAbsolutePath(), file.getName()));
					//ib.filepath = ib.getThumbnailPath();

					datas.add(ib);
				}
				sortModifiedTime(datas);
			}
			return datas;
		}
		
		private void sortModifiedTime(ArrayList<MediaBean> albumBeans) {

			if (albumBeans == null) {
				return;
			}

			Collections.sort(albumBeans, new Comparator<MediaBean>() {
				@Override
				public int compare(MediaBean lhs, MediaBean rhs) {
					return lhs.getModified() <= rhs.getModified() ? 1 : -1;
				}
			});

		}
		
		@Override
		protected void onPostExecute(List<MediaBean> imageBeans) {
			super.onPostExecute(imageBeans);
			if(imageBeans == null){
				imageBeans = new ArrayList<MediaBean>();
			}
			/*
			if(imageBeans.size() == 0){
				Intent intent = new Intent(AllVideoBrowserActivity.this, RecorderVideoActivity.class);
				startActivityForResult(intent, REQUEST_START_VIDEO);
				return;
			}
			*/
			mVideoDatas.clear();
			mVideoDatas.addAll(imageBeans);
			
			AlbumBean ab = new AlbumBean();
			ab.setImageBeans(mVideoDatas);
			mAllVideoFragment.setAlbumBean(ab, 0);
		}
	}
}