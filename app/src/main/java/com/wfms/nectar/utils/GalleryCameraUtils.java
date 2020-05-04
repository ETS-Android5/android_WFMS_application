package com.wfms.nectar.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by aashita on 11/08/17.
 */

public class GalleryCameraUtils {

    private static final String TAG = GalleryCameraUtils.class.getSimpleName();

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PICK_GALLERY_ACTION = 100;
    public static final int REQUEST_CAMERA_PERMISSION = 123;
    public static final int REQUEST_GALLERY_PERMISSION = 345;

    public static String realPath = "";
    private static GalleryCameraUtils mGalleryCameraObj;

    public interface RequestPermission{

        void storagePermissionListener();
    }

    public static void openGallery(Context mContext) {


        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        // intent.setType("image*//*");
        ((AppCompatActivity) mContext).startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_GALLERY_ACTION);


        //int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);

       /* if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ((CreateCollaborateActivity) mContext).storagePermissionListener();
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            // intent.setType("image*//**//*");
            ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_GALLERY_ACTION);
        }
*/
    }

public static void pdf(Context con)
{
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    String[] mimeTypes =
            {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                    "application/pdf"};
    intent.setType("*/*");
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
    ((AppCompatActivity)con).startActivityForResult(Intent.createChooser(intent,"ChooseFile"), AppConstants.REQUEST_OPEN_DOCUMENTS_FOLDER);
}

    public static void openDocumentsFolder(Context context){

        /*String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};*/

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      //  Intent intent = new Intent("android.intent.action.MULTIPLE_PICK");
       // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
       // intent.putExtra(Constants.SELECTION_MODE,Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
       // intent.addCategory(Intent.CATEGORY_OPENABLE);
      /*  Intent intent = new Intent(context, FileChooser.class);
        intent.putExtra(Constants.SELECTION_MODE,Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());*/


        intent.addCategory(Intent.CATEGORY_DEFAULT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }




        String authorityName = context.getPackageName() + ".provider";
        File mImageF = FileUtils.createTempPdfFile(context);
        Uri imageUri = FileProvider.getUriForFile(context, authorityName, mImageF);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        ((AppCompatActivity)context).startActivityForResult(Intent.createChooser(intent,"ChooseFile"), AppConstants.REQUEST_OPEN_DOCUMENTS_FOLDER);

    }


   /* public static void openDocumentsFolder1(Context context){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
       // intent.setType("***///*//**//*");
      //  ((Activity)context).startActivityForResult(intent, AppConstants.REQUEST_OPEN_DOCUMENTS_FOLDER);
   // }*/


    public static boolean isDeviceSupportCamera(Context mContext) {
        if (mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static void dispatchTakePictureIntent(Context mContext) {
        /*int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                ((Activity) mContext).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }*/

        //permission already checked
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            ((AppCompatActivity) mContext).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    // upload image

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Uri uri, Context mContext) { // return file path
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mContext.getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor == null || cursor.getCount() < 1) {
                return "";
            }
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (columnIndex < 0) { // no column index
                return "";
            }
            return cursor.getString(columnIndex);
        }
        catch (Exception e){
           // LoggerUtils.crashlyticsLog(TAG, e.getMessage());
        }
        return null;
    }


    public static String bitmapToFileConverter(Context mCtx, Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            // options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), mBitmap.getHeight(),
                    true);
            File file = new File(mCtx.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out =
                    mCtx.openFileOutput(file.getName(),
                            Context.MODE_PRIVATE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            realPath = file.getAbsolutePath();
            /*File f = new File(realPath);
            uri = Uri.fromFile(f);*/          // used when need Uri in return

        } catch (Exception e) {
            //LoggerUtils.logE(TAG, e.getMessage());
        }
        return realPath;
    }

    public static File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
