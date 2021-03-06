package com.talenton.lsg.base.util;

/**
 * Created by ttt on 2016/4/18.
 */
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.content.ContentUris;
import android.text.TextUtils;

public class FileUtil {

    /** 拍摄图片目录 */
    public static final String PICTURE_DIR = "baobao";
    public static final String PICTURE_DOC = "doc";
    /** 媒体文件前缀 */
    public static final String MESSAGE_PREFIX = "msg_";
    /** 图片文件后缀 */
    public static final String PICTURE_SUFFIX = ".jpg";
    /** 录制视频目录 */
    public static final String VIDEO_RECORD = "recordvideo";

    public static final String[] INTERNAL_STORAGE_PATHS = new String[] { "/mnt/", "/emmc/" };
    public static final String PATH = "data/";
    public static final String PIC_PATH = PATH + "pic/.nomedia/";
    private static String fileSystemDir;
    private static String internalStoragePath;
    private static String fileSystemCacheDir;
    private static String picPath;

    private FileUtil() {
    }

    public static void initStoragePath(Context context) {
        initInternalStoragePath();
        fileSystemCacheDir = context.getCacheDir().getAbsolutePath().concat(File.separator);
        fileSystemDir = context.getFilesDir().getAbsolutePath().concat(File.separator);
        picPath = fileSystemDir + PIC_PATH;

        String storagePath = null;
        if (isSDCardMounted()) {
            Environment.getExternalStorageDirectory().getAbsolutePath();
            storagePath = context.getExternalFilesDir(null).getAbsolutePath();
        } else if (null != internalStoragePath) {
            storagePath = internalStoragePath;
        }
        if (null != storagePath) {
            picPath = storagePath.concat(File.separator) + PIC_PATH;
        }
        File picDir = new File(picPath);
        if (!picDir.exists()) {
            picDir.mkdirs();
        }
    }

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

    public static String getPicPath() {
        return picPath;
    }

    /**
     * 获取文件大小的描述文本，如1.6K  2M
     *
     * @param filePath 文件路径
     * @return 返回文件大小描述文本
     */
    public static String getFileSizeString(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        long fileSize = file.length();
        return getFileSizeString(fileSize);
    }

    /**
     * 字节数转换为KB/MB/...
     *
     * @param fileLength
     * @return
     */
    public static String getFileSizeString(long fileLength) {
        String[] posFixes = new String[]{"B", "KB", "MB", "GB","TB"};
        for (int i = 0; i < posFixes.length; i++) {
            long top = (long) Math.pow(1024, (i + 1));
            long scale = (long) Math.pow(1024, i);
            if (fileLength < top) {
                String result = String.format("%1$.2f", (double)fileLength / scale);
                if (result.endsWith(".00")) {
                    result = result.replace(".00", "");
                }
                return result + posFixes[i];
            }
        }
        return null;
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (!file.exists()) {
            return size;
        }
        if (!file.isDirectory()) {
            size = file.length();
        } else {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFileSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        }
        return size;
    }

    public static List<File> listFiles(File dir) {
        if (!dir.isDirectory()) return new ArrayList<File>();

        List<File> files = new ArrayList<File>();
        File[] fileArray = dir.listFiles();
        if (fileArray != null) {
            for (File file : fileArray) {
                if (!file.isDirectory()) {
                    files.add(file);
                } else {
                    List<File> subFiles = listFiles(file);
                    files.addAll(subFiles);
                }
            }
        }
        return files;
    }

    public static byte[] fileToBytes(File file) {
        if (Checker.isEmpty(file)) return null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE) return null; //throw new Exception("File is too large");

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
                offset += numRead;

            // Ensure all the bytes have been read in
            if (offset < bytes.length)
                AppLogger.w("Could not completely read file " + file);
            return bytes;
        } catch (IOException e) {
            AppLogger.e("Failed to read file " + file, e);
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                AppLogger.e("Could not close stream", e);
            }
        }
    }

    public static String fileToString(File file) {
        if (Checker.isEmpty(file)) return null;
        return new String(fileToBytes(file));
    }

    public static File bytesToFile(byte[] bytes, File file) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null && !file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            bos.flush();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            AppLogger.e("Failed to save file " + file, e);
            return null;
        } finally {
            try {
                fos.close();
                bos.close();
            } catch (IOException e) {
                AppLogger.e("Could not close stream", e);
            }
        }
        return file;
    }

    public static File stringToFile(String string, File file) {
        Writer out = null;
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null && !file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            out = new OutputStreamWriter(new FileOutputStream(file));
            out.write(string);
            out.flush();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            AppLogger.e("Failed to save file " + file, e);
            return null;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                AppLogger.e("Could not close stream", e);
            }
        }
        return file;
    }

    public static File appendStringToFile(String string, File file) {
        Writer out = null;
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null && !file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            out = new OutputStreamWriter(new FileOutputStream(file, true));
            out.write(string, 0, string.length());
            out.flush();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            AppLogger.e("Failed to save file " + file, e);
            return null;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                AppLogger.e("Could not close stream", e);
            }
        }
        return file;
    }

    public static boolean copyFile(File src, File dst) {
        if (!Checker.isExistedFile(src)) return false;
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dst);

            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            os.flush();
            return true;
        } catch (Exception e) {
            AppLogger.e("Failed to copy file from " + src + " to " + dst, e);
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                AppLogger.e("Could not close stream", e);
            }
        }
        return false;
    }

    public static boolean copyFolder(File src, File dst) {
        if (!Checker.isExistedFile(src)
                || !src.isDirectory()) return false;
        boolean result = true;
        if (src.exists()) dst.mkdirs();
        File[] fileArray = src.listFiles();
        if (fileArray != null) {
            for (File file : src.listFiles()) {
                File dstFile = new File(dst, file.getName());
                if (file.isDirectory()) {
                    result = copyFolder(file, dstFile);
                    if (!result) {
                        break;
                    }
                } else {
                    result = copyFile(file, dstFile);
                    if (!result) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static void deleteFolder(File dir) {
        deleteFolder(dir, null);
    }

    public static void deleteFolder(File dir, String skipFilePattern) {
        if (!Checker.isExistedFile(dir)) return;
        if (!dir.isDirectory()) throw new IllegalArgumentException();

        File[] fileArray = dir.listFiles();
        if (fileArray != null) {
            for (File fileToBeDeleted : dir.listFiles()) {
                if (fileToBeDeleted.isDirectory()) deleteFolder(fileToBeDeleted, skipFilePattern);
                if (!Checker.isEmpty(skipFilePattern)
                        && fileToBeDeleted.getName().matches(skipFilePattern)) continue;
                fileToBeDeleted.delete();
            }
        }

        // ref: http://stackoverflow.com/questions/11539657
//        final File tmp = new File(dir.getAbsolutePath() + System.currentTimeMillis());
//        dir.renameTo(tmp);
//        tmp.delete();
        dir.delete();
    }

    public static void deleteFile(File file) {
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public static String getFileNameWithoutExt(File file) {
        if (file == null) return null;
        return getFileNameWithoutExt(file.getName());
    }

    public static String getFileNameWithoutExt(String filename) {
        if (Checker.isEmpty(filename)) return null;
        if (filename.lastIndexOf('.') < 0) return filename;
        return filename.substring(0, filename.lastIndexOf('.'));
    }

    public static String getFileExtName(File file) {
        if (file == null) return null;
        String fileName = file.getName();
        if (fileName.lastIndexOf('.') < 0) return fileName;
        return fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
    }

    public static File addSuffixToFileName(File file, String suffix) {
        if (file == null) return null;
        return new File(file.getParent(),
                getFileNameWithoutExt(file) + suffix + "." + getFileExtName(file));
    }

    public static String getFileNameFromUrl(String url) {
        if (Checker.isEmpty(url)) return null;
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    public static String getFilePathFromDir(File file, File dir) {
        if (Checker.isExistedFile(file)) return null;
        return file.getAbsolutePath().substring(dir.getAbsolutePath().length());
    }

    public static boolean prepareParentDir(File file) {
        return file.getParentFile().exists() || file.getParentFile().mkdirs();
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

    public static File getExternalPath(final Context context, String name){
        File cacheDir = getCacheDirectory(context);
        File dir = new File(cacheDir, name);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public static File getImgExternalPath(final Context context){
        File cacheDir = getCacheDirectory(context);
        File dir = new File(cacheDir, "img");
        if(!dir.exists())
            dir.mkdirs();
        return dir;
    }

    /**
     * 用当前时间生成文件名
     */
    public static String genUploadFileName(String suffix) {
        return MESSAGE_PREFIX + System.currentTimeMillis() + suffix;
    }

    //StoragePathHelper
    public static File getCaptureFile(final Context context){
        File cacheDir = getCacheDirectory(context);
        File dir = new File(cacheDir, "cropimage");
        if(!dir.exists())
            dir.mkdirs();


        File captureFile = new File(dir, "crop"+ UUID.randomUUID().toString()+".jpg");
            if(captureFile.exists())
                captureFile.delete();
        return captureFile;
    }

    public static String getPathFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        File file = new File(uri.getPath());
        if (file.isFile()) {
            return file.getPath();
        }
        if (uri.toString().indexOf("file://") == 0) {
            String ret = uri.toString().substring(7);
            ret = decodeUri(ret);
            return ret;
        } else {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String ret = cursor.getString(0);
                ret = decodeUri(ret);
                cursor.close();
                return ret;
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static String decodeUri(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return uri;
        }
        int index = uri.indexOf('%');
        if (index != -1) {
            uri = Uri.decode(uri);
        }
        return uri;
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

    /**
     * 判断文件是否可读写
     *
     * @param filePath
     * @return
     */
    public static boolean isFileCanReadAndWrite(String filePath) {
        if (null != filePath && filePath.length() > 0) {
            File f = new File(filePath);
            if (null != f && f.exists()) {
                return f.canRead() && f.canWrite();
            }
        }
        return false;
    }


}
