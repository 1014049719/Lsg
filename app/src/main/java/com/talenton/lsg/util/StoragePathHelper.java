package com.talenton.lsg.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.FileUtil;

/**
 * 程序用到的外部存储路径定义类
 * 
 * @author yellow
 * @version 2015年3月13日 下午52:10:36
 */
public class StoragePathHelper {

	public static final String[] INTERNAL_STORAGE_PATHS = new String[] { "/mnt/", "/emmc/" };
	public static final String PATH = "data/";
	public static final String LOG_PATH = PATH + ".log/";
	public static final String CACHE_PATH = PATH + ".cache/";
	public static final String VIDEO_PATH = ".video/";
	public static final String PIC_PATH = PATH + "pic/.nomedia/";

	private static String fileSystemDir;
	private static String internalStoragePath;

	private static String parentPath;
	private static String logPath;
	private static String picPath;
	private static String cachePath;
	private static String videoPath;
	private static String fileSystemCacheDir;

	private static void initInternalStoragePath() {
		if (isSDCardMounted()) {
			return;
		}
		for (String path : INTERNAL_STORAGE_PATHS) {
			if (FileUtil.isFileCanReadAndWrite(path)) {
				internalStoragePath = path;
				return;
			} else {
				File f = new File(path);
				if (f.isDirectory()) {
					for (File file : f.listFiles()) {
						if (file != null && file.isDirectory() && !file.isHidden()
								&& FileUtil.isFileCanReadAndWrite(file.getPath())) {
							internalStoragePath = file.getPath();
							if (!internalStoragePath.endsWith(File.separator)) {
								internalStoragePath += File.separator;
							}
							return;
						}
					}
				}
			}
		}
	}

	private static void getExternalSdPath() {
		Map<String, String> map = System.getenv();
		// 遍历出来可以看到最后一项是外置SD卡路径
		Set<String> set = map.keySet();
		Iterator<String> key = set.iterator();

		Collection<String> col = map.values();
		Iterator<String> val = col.iterator();
		while (key.hasNext())
			AppLogger.d("path", key.next() + "=" + val.next());
	}

	/**
	 * @Title: getExtSDCardPaths
	 * @Description: to obtain storage paths, the first path is theoretically
	 *               the returned value of
	 *               Environment.getExternalStorageDirectory(), namely the
	 *               primary external storage. It can be the storage of internal
	 *               device, or that of external sdcard. If paths.size() >1,
	 *               basically, the current device contains two type of storage:
	 *               one is the storage of the device itself, one is that of
	 *               external sdcard. Additionally, the paths is directory.
	 * @return List<String>
	 * @throws IOException
	 */
	public static List<String> getExtSDCardPaths() {
		List<String> paths = new ArrayList<String>();
		String extFileStatus = Environment.getExternalStorageState();
		File extFile = Environment.getExternalStorageDirectory();
		if (extFileStatus.equals(Environment.MEDIA_MOUNTED) && extFile.exists() && extFile.isDirectory()
				&& extFile.canWrite()) {
			AppLogger.d("path0000", extFile.getAbsolutePath());
			paths.add(extFile.getAbsolutePath());
		}
		try {
			// obtain executed result of command line code of 'mount', to judge
			// whether tfCard exists by the result
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("mount");
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			int mountPathIndex = 1;
			while ((line = br.readLine()) != null) {
				// format of sdcard file system: vfat/fuse
				AppLogger.d("path1111", line);
				if ((!line.contains("fat") && !line.contains("fuse") && !line.contains("storage"))
						|| line.contains("secure") || line.contains("asec") || line.contains("firmware")
						|| line.contains("shell") || line.contains("obb") || line.contains("legacy")
						|| line.contains("data")) {
					continue;
				}
				String[] parts = line.split(" ");
				int length = parts.length;
				if (mountPathIndex >= length) {
					continue;
				}
				String mountPath = parts[mountPathIndex];
				if (!mountPath.contains("/") || mountPath.contains("data") || mountPath.contains("Data")) {
					continue;
				}
				File mountRoot = new File(mountPath);
				if (!mountRoot.exists() || !mountRoot.isDirectory() || !mountRoot.canWrite()) {
					continue;
				}
				boolean equalsToPrimarySD = mountPath.equals(extFile.getAbsolutePath());
				if (equalsToPrimarySD) {
					continue;
				}
				AppLogger.d("path2222", mountPath);
				paths.add(mountPath);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paths;
	}

	/**
	 * 检查 SDCard 是否装载
	 */
	public static boolean isSDCardMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * SD卡剩余空间大小
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static long getSDFreeSize() {
		if (!isSDCardMounted()) {
			return 0;
		}
		long blockSize;
		//long totalBlocks;
		long availableBlocks;
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
		{
			blockSize = stat.getBlockSizeLong();
			//totalBlocks = stat.getBlockCountLong();
			availableBlocks = stat.getAvailableBlocksLong();
		}
		else
		{
			blockSize = stat.getBlockSize();
			//totalBlocks = stat.getBlockCount();
			availableBlocks = stat.getAvailableBlocks();
		}
		return (availableBlocks * blockSize);
	}

	/**
	 * SD卡总容量
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public long getSDAllSize() {
		if (!isSDCardMounted()) {
			return 0;
		}
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		long blockSize = sf.getBlockSize();
		long allBlocks = sf.getBlockCount();
		return (allBlocks * blockSize);
	}

	public static void initStoragePath(Context context) {
		initInternalStoragePath();
		fileSystemCacheDir = context.getCacheDir().getAbsolutePath().concat(File.separator);
		fileSystemDir = context.getFilesDir().getAbsolutePath().concat(File.separator);
		parentPath = fileSystemDir + PATH;
		logPath = fileSystemDir + LOG_PATH;
		cachePath = fileSystemDir + CACHE_PATH;
		videoPath = fileSystemDir + VIDEO_PATH;
		picPath = fileSystemDir + PIC_PATH;

		String storagePath = null;
		if (isSDCardMounted()) {
			Environment.getExternalStorageDirectory().getAbsolutePath();
			storagePath = context.getExternalFilesDir(null).getAbsolutePath();
		} else if (null != internalStoragePath) {
			storagePath = internalStoragePath;
		}
		if (null != storagePath) {
			parentPath = storagePath.concat(File.separator) + PATH;
			logPath = storagePath.concat(File.separator) + LOG_PATH;
			cachePath = storagePath.concat(File.separator) + CACHE_PATH;
			videoPath = storagePath.concat(File.separator) + VIDEO_PATH;
			picPath = storagePath.concat(File.separator) + PIC_PATH;
		}

		File pathDir = new File(parentPath);
		if (!pathDir.exists()) {
			pathDir.mkdirs();
		}

		File logDir = new File(logPath);
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		File cacheDir = new File(cachePath);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		File videoDir = new File(videoPath);
		if (!videoDir.exists()) {
			videoDir.mkdirs();
		}
		File picDir = new File(picPath);
		if (!picDir.exists()) {
			picDir.mkdirs();
		}
	}

	public static String getParentPath() {
		return parentPath;
	}

	public static String getPicPath() {
		return picPath;
	}

	public static String getLogPath() {
		return logPath;
	}

	public static String getCachePath() {
		return cachePath;
	}

	public static String getVideoPath() {
		return videoPath;
	}

	public static String getFileSystemDir() {
		return fileSystemDir;
	}

	public static void setFileSystemDir(String fileSystemDir) {
		StoragePathHelper.fileSystemDir = fileSystemDir;
	}

	public static String getFileSystemCacheDir() {
		return fileSystemCacheDir;
	}

	public static void setFileSystemCacheDir(String fileSystemCacheDir) {
		StoragePathHelper.fileSystemCacheDir = fileSystemCacheDir;
	}
    
    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens
            externalStorageState = "";
        }
        if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
        	appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }
    
    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
        }
        return appCacheDir;
    }
    
    public static File getVideoExternalPath(final Context context){
    	
    	File cacheDir = getCacheDirectory(context);
    	File dir = new File(cacheDir, "video");
    	if(!dir.exists())
    		dir.mkdirs();
    	return dir;
    	
    	/*
    	File file = new File(Environment.getExternalStorageDirectory(),
				 "/com.bbg/video/a.txt");
    	
    	if(!file.getParentFile().exists()){
    		file.getParentFile().mkdirs();
    	}
    	return file.getAbsolutePath().replace("a.txt", "");
    	*/
    }
    
  //StoragePathHelper
    public static File getCaptureFile(final Context context){
    	File cacheDir = getCacheDirectory(context);
    	File dir = new File(cacheDir, "cropimage");
    	if(!dir.exists())
    		dir.mkdirs();
    	
    	
    	File captureFile = new File(dir, "crop"+UUID.randomUUID().toString()+".jpg");
    	try {
    		if(captureFile.exists())
    			captureFile.delete();
			captureFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return captureFile;
    }
    
    private static String getPathDeprecated(Context context, Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            //Log.d("getPathDeprecated", cursor.getString(column_index)+"|"+uri.getPath());
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    @SuppressLint("NewApi")
	public static String getSmartFilePath(Context context, Uri uri){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getPathDeprecated(context, uri);
        }
        return  getPath(context, uri);
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    
    public static File getUploadFile(){
    	File f = new File(Environment.getExternalStorageDirectory(),
				 "crop_cache_file.jpg");
    	return f;
    }
}
