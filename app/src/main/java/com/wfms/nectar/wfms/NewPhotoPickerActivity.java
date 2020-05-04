package com.wfms.nectar.wfms;

import android.Manifest;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.GalleryCameraUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public abstract class NewPhotoPickerActivity extends AppCompatActivity {
    public static final String TAG = NewPhotoPickerActivity.class.getCanonicalName();
    private static final int PHOTO_PICKING_CODE = 211;
    private static final int REQUEST_PERMISSIONS_CODE = 212;
    private String[] requiredPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private File mImageF;
    private String mChooserTitle = "Capture  image";
    int count=1;
    public static ArrayList<String> imagesEncodedList = new ArrayList<>();

    protected void initImagePickProcess(AppCompatActivity activity, @Nullable String chooserTitle) {
        if (chooserTitle != null) mChooserTitle = chooserTitle;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(
                        activity,
                        "Please grant access to these permissions in orde" +
                                "r to edit your Image",
                        Toast.LENGTH_SHORT).show();
            }
            requestPermissions();
        } else {
            initPhotoPickerIntent();
        }
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_PERMISSIONS_CODE);
    }

    private void initPhotoPickerIntent() {
        try {
            List<Intent> matchingIntents = new ArrayList<>();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String authorityName =  "com.wfms.nectar.wfms.provider";
            mImageF = createTempImageFile(this);
            Log.d("mImageF",""+mImageF);
            Uri imageUri = FileProvider.getUriForFile(this, authorityName, mImageF);

            for (ResolveInfo info : this.getPackageManager().queryIntentActivities(cameraIntent, 0)) {
                Intent intent = new Intent(cameraIntent);
                intent.setPackage(info.activityInfo.packageName);
                intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                if (imageUri != null) intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                matchingIntents.add(intent);
            }



            for (Intent intent : matchingIntents) {
                if (intent.getComponent() != null && intent.getComponent().getClassName().equalsIgnoreCase(AppConstants.ANDROID_DOCUMENTS_ACTIVITY)) {
                    matchingIntents.remove(intent);
                    break;
                }
            }

            Intent photoPickingIntent = Intent.createChooser(matchingIntents.get(matchingIntents.size() - 1), mChooserTitle);
            startActivityForResult(photoPickingIntent, PHOTO_PICKING_CODE);
            this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initPhotoPickerIntent();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PHOTO_PICKING_CODE:

                // a gallery image
                if (data != null && data.getData() != null) {


                   String[] filePathColumn1 = {MediaStore.Images.Media.DATA};
                    // a camera image
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = this.getContentResolver().query(uri, filePathColumn1, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn1[0]);
                            String imageEncoded = cursor.getString(columnIndex);
                            MainActivity.imagesEncodedList.add(imageEncoded);
                            cursor.close();
                            File file = new File(imageEncoded);
                            onNewImagePickSuccess(FileProvider.getUriForFile(this, "com.wfms.nectar.wfms.provider", file), file);
                        }

                    }
                    else
                    {
                        String absoluteFilePath = GalleryCameraUtils.getRealPathFromURI(data.getData(), this);
                        if(absoluteFilePath!=null)
                        {
                            MainActivity.imagesEncodedList.add(absoluteFilePath);
                        }
                        onNewImagePickSuccess(data.getData(), mImageF);
                    }
                } else {
                    if(data!=null) {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        // a camera image
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayUri.add(uri);
                                // Get the cursor
                                Cursor cursor = this.getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String imageEncoded = cursor.getString(columnIndex);
                                MainActivity.imagesEncodedList.add(imageEncoded);
                                cursor.close();
                                File file = new File(imageEncoded);
                                onNewImagePickSuccess(FileProvider.getUriForFile(this, "com.wfms.nectar.wfms.provider", file), file);

                            }

                        }
                    }
                    else
                    {
                        String absoluteFilePath = mImageF.getAbsolutePath();

                        MainActivity.imagesEncodedList.add(absoluteFilePath);
                        onNewImagePickSuccess(FileProvider.getUriForFile(this, "com.wfms.nectar.wfms.provider", mImageF), mImageF);
                    }

                }
                break;
        }
    }

    public String getImagePath(Uri uri){
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor =this. getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();


        return path;
    }
    protected abstract void onNewImagePickSuccess(Uri uri, File tempFile);

   public static File createTempImageFile(Context context) {
        String timestamp = new SimpleDateFormat("ddMMyyyy_HH:mm:ss", Locale.getDefault()).format(new Date());

        String fileName = "capture" ;
        File storageDir = context.getExternalCacheDir();
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    fileName,
                    ".jpg",
                    storageDir);
        } catch (IOException e) {

        }

        if (imageFile != null && !imageFile.exists()) imageFile.mkdirs();
        return imageFile;
    }


}
