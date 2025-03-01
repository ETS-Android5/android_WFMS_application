package com.wfms.nectar.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.ContactsContract.AUTHORITY;
import static android.provider.DocumentsContract.getDocumentId;
import static android.provider.DocumentsContract.isDocumentUri;

/*
 * This class is created by aashita on 26/07/2017
 *
 */


public class FileUtils {

    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    public static final String MIME_TYPE_APP = "application/*";
    public static final String HIDDEN_PREFIX = ".";
    /**
     * TAG for log messages.
     */
    static final String TAG = "FileUtils";
    private static final boolean DEBUG = true; // Set to true to enable logging
    /**
     * File and folder comparator.
     *
     * @author paulburke
     */
   /* public static Comparator<File> sComparator = (f1, f2) -> {
        // Sort alphabetically by lower case, which is much cleaner
        return f1.getName().toLowerCase().compareTo(
                f2.getName().toLowerCase());
    };
    *//**
     * File (not directories) filter.
     *
     * @author paulburke
     *//*
    public static FileFilter sFileFilter = file -> {
        final String fileName = file.getName();
        // Return files only (not directories) and skip hidden files
        return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
    };
    *//**
     * Folder (directories) filter.
     *
     * @author paulburke
     *//*
    public static FileFilter sDirFilter = file -> {
        final String fileName = file.getName();
        // Return directories only and skip hidden directories
        return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
    };*/

    private FileUtils() {
    } //private constructor to enforce Singleton pattern

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal(String url) {
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        return false;
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    public static File getPathWithoutFilename(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname = filepath.substring(0,
                        filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    /**
     * @return The MIME type for the given file.
     */
    public static String getMimeType(File file) {
        return getMimeType(file.getName());
    }

    /**
     * @return The MIME type for the given file name.
     */
    public static String getMimeType(String fileName) {

        String extension = getExtension(fileName);

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }

    public static String getMimeType1(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * @return The MIME type for the give Uri.
     */
    public static String getMimeType(Context context, Uri uri) {
        File file = new File(getPath(context, uri));
        return getMimeType(file);
    }

    /**
     * @param uri The Uri to check.
     * @author paulburke
     */
    public static boolean isLocalStorageDocument(Uri uri) {
        return AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Log.w(TAG, uri.toString());

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

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
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     * @see #isLocal(String)
     * @see #getFile(Context, Uri)
     */
    public static String getPath(final Context context, final Uri uri) {

        if (DEBUG)
            Log.d(TAG + " File -",
                    "Authority: " + uri.getAuthority() +
                            ", Fragment: " + uri.getFragment() +
                            ", Port: " + uri.getPort() +
                            ", Query: " + uri.getQuery() +
                            ", Scheme: " + uri.getScheme() +
                            ", Host: " + uri.getHost() +
                            ", Segments: " + uri.getPathSegments().toString()
            );

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && isDocumentUri(context, uri)) {
                // LocalStorageProvider
                if (isLocalStorageDocument(uri)) {
                    // The path is the id
                    return getDocumentId(uri);
                }
                // ExternalStorageProvider
                else if (isExternalStorageDocument(uri)) {
                    final String docId = getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = getDocumentId(uri);
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
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see #getPath(Context, Uri)
     */
    public static File getFile(Context context, Uri uri) {
        try {
            if (uri != null) {
                String path = getPath(context, uri);
                if (path != null && isLocal(path)) {
                    return new File(path);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param file
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri) {
        return getThumbnail(context, uri, getMimeType(context, uri));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @param mimeType
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
        if (DEBUG)
            Log.d(TAG, "Attempting to get thumbnail");

        if (!isMediaUri(uri)) {
            Log.e(TAG, "You can only retrieve thumbnails for images and videos.");
            return null;
        }

        Bitmap bm = null;
        if (uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    final int id = cursor.getInt(0);
                    if (DEBUG)
                        Log.d(TAG, "Got thumb ID: " + id);

                    if (mimeType.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Video.Thumbnails.MINI_KIND,
                                null);
                    } else if (mimeType.contains(FileUtils.MIME_TYPE_IMAGE)) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Images.Thumbnails.MINI_KIND,
                                null);
                    }
                }
            } catch (Exception e) {
                if (DEBUG)
                    Log.e(TAG, "getThumbnail", e);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return bm;
    }

    /**
     * save file
     *
     * @param path
     * @return
     */
    public static Uri saveFile(String path) {

        File mFile = null;
        InputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(path);
            String filename = path.substring(path.lastIndexOf("/") + 1);
            mFile = getOutputMediaFile(AppConstants.IMAGES_DIR, filename);
            out = new FileOutputStream(mFile, false);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            Log.e(TAG, "", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
        }
        return getUri(mFile);
    }

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     * @author paulburke
     */
    public static void createGetContentIntent(boolean localOnly, Context context) {
        // Implicitly allow the user to select a particular kind of data
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // The MIME data type filter
        intent.setType("*/*");
        // show only local files or all
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, localOnly);
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        ((AppCompatActivity) context).startActivityForResult(intent, AppConstants.REQUEST_OPEN_DOCUMENTS_FOLDER);
    }

    public static File getOutputMediaFile(String directory, String filename) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + AppConstants.APPLICATION_DIR_NAME, directory);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + filename);
    }

    public static Uri getFilePath(Context context, Uri uri) {
        return Uri.parse("file://" + FileUtils.getPath(context, uri));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static File getRealPathFromURI_API19(Context context, Uri uri) {
        try {
            String filePath = "";

            if (uri != null) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                    } else {
                        File extenal[] = context.getExternalMediaDirs();
                        if (extenal.length > 1) {
                            filePath = extenal[1].getAbsolutePath();
                            filePath = filePath.substring(0, filePath.indexOf("Android")) + split[1];
                        }
                        if (filePath != null && isLocal(filePath)) {
                            return new File(filePath);
                        }
                    }

                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    //final Uri contentUri = ContentUris.withAppendedId(
                    // Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    Cursor cursor = null;
                    final String column = "_data";
                    final String[] projection = {column};

                    try {
                        cursor = context.getContentResolver().query(uri, projection, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            final int index = cursor.getColumnIndexOrThrow(column);
                            String result = cursor.getString(index);
                            cursor.close();
                            return new File(result);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                } else if (DocumentsContract.isDocumentUri(context, uri)) {
                    // MediaProvider
                    String wholeID = DocumentsContract.getDocumentId(uri);

                    // Split at colon, use second item in the array
                    String[] ids = wholeID.split(":");
                    String id;
                    String type;
                    if (ids.length > 1) {
                        id = ids[1];
                        type = ids[0];
                    } else {
                        id = ids[0];
                        type = ids[0];
                    }

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    } else {
                        contentUri = uri;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{id};
                    final String column = "_data";
                    final String[] projection = {column};
                    Cursor cursor = context.getContentResolver().query(contentUri,
                            projection, selection, selectionArgs, null);

                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndex(column);

                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        }
                        cursor.close();
                    }
                    if (filePath != null && isLocal(filePath)) {
                        return new File(filePath);
                    }
                } else {
                    String[] proj = {MediaStore.Audio.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                        if (cursor.moveToFirst())
                            filePath = cursor.getString(column_index);
                        cursor.close();
                    }

                    if (filePath != null && isLocal(filePath)) {
                        return new File(filePath);
                    }
                    // return filePath;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createTempImageFile(Context context) {
        String timestamp = new SimpleDateFormat("ddMMyyyy_HH:mm:ss", Locale.getDefault()).format(new Date());
        String fileName = "TagTaste_" + timestamp;
        File storageDir = context.getExternalCacheDir();
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                     fileName,
                    ".jpg",
                    storageDir);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        if (imageFile != null && !imageFile.exists()) imageFile.mkdirs();
        return imageFile;
    }

    public static File createTempVideoFile(Context context) {
        String timestamp = new SimpleDateFormat("ddMMyyyy_HH:mm:ss", Locale.getDefault()).format(new Date());
        String fileName = "TagTaste_" + timestamp;
        File storageDir = context.getExternalCacheDir();
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    fileName,
                    ".mp4",
                    storageDir);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        if (imageFile != null && !imageFile.exists()) imageFile.mkdirs();
        return imageFile;
    }

    public static File createTempPdfFile(Context context) {
        String timestamp = new SimpleDateFormat("ddMMyyyy_HH:mm:ss", Locale.getDefault()).format(new Date());
        String fileName = timestamp;
        File storageDir = context.getExternalCacheDir();
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    fileName,
                    ".pdf",
                    storageDir);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        if (imageFile != null && !imageFile.exists()) imageFile.mkdirs();
        return imageFile;
    }

    public static String convertMediaUriToPath(Uri uri, Context context) {
        String[] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    public static String getFileName(Context context, Uri uri) {
        if (uri != null && context != null) {
            Cursor returnCursor =
                    context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                if (nameIndex >= 0 && sizeIndex >= 0) {
                    Log.d(TAG, "File Name : " + returnCursor.getString(nameIndex));
                    Log.d(TAG, "File Size : " + Long.toString(returnCursor.getLong(sizeIndex)));
                    // Boolean isValidFile = checkFormat(returnCursor.getString(nameIndex));

                    // if (!isValidFile) {
                    return returnCursor.getString(nameIndex);
                    // }
                }
            }
        }
        return "";
    }

}