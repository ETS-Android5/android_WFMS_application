package com.wfms.nectar.wfms;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.File;
import java.io.FileNotFoundException;

public class FaceActivity extends AppCompatActivity implements View.OnClickListener {

        ImageView imageView, imgTakePicture;
        Button btnProcessNext, btnTakePicture;
        TextView txtSampleDesc, txtTakenPicDesc;
        private FaceDetector detector;
        Bitmap editedBitmap;
        int currentIndex = 0;
        int[] imageArray;
        private Uri imageUri;
        private static final int REQUEST_WRITE_PERMISSION = 200;
        private static final int CAMERA_REQUEST = 101;

      private static final String SAVED_INSTANCE_URI = "uri";
      private static final String SAVED_INSTANCE_BITMAP = "bitmap";

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face1);

        imageArray = new int[]{R.drawable.sample_1, R.drawable.sample_2, R.drawable.sample_3};
        detector = new FaceDetector.Builder(getApplicationContext())
        .setTrackingEnabled(false)
        .setLandmarkType(FaceDetector.ALL_CLASSIFICATIONS)
        .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
        .build();
         initViews();
        }

      private void initViews() {
        imageView = (ImageView) findViewById(R.id.imageView);
        imgTakePicture = (ImageView) findViewById(R.id.imgTakePic);
        btnProcessNext = (Button) findViewById(R.id.btnProcessNext);
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
        txtSampleDesc = (TextView) findViewById(R.id.txtSampleDescription);
        txtTakenPicDesc = (TextView) findViewById(R.id.txtTakePicture);

        processImage(imageArray[currentIndex]);
        currentIndex++;

        btnProcessNext.setOnClickListener(this);
        btnTakePicture.setOnClickListener(this);
        imgTakePicture.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnProcessNext:
        imageView.setImageResource(imageArray[currentIndex]);
        processImage(imageArray[currentIndex]);
        if (currentIndex == imageArray.length - 1)
        currentIndex = 0;
        else
        currentIndex++;

        break;

        case R.id.btnTakePicture:
        ActivityCompat.requestPermissions(FaceActivity.this, new
        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        break;

        case R.id.imgTakePic:
        ActivityCompat.requestPermissions(FaceActivity.this, new
        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        break;
        }
        }

      @Override
       public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
        case REQUEST_WRITE_PERMISSION:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        startCamera();
        } else {
        Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
        }
        }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

              launchMediaScanIntent();

              try {

                  processCameraPicture();
              } catch (Exception e) {
                  Toast.makeText(getApplicationContext(), "Failed to load Image", Toast.LENGTH_SHORT).show();
              }
          }
}

        private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
        }

        private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
        imageUri = FileProvider.getUriForFile(this, "com.wfms.nectar.wfms" + ".provider", photo);
       // imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*if (imageUri != null) {
        outState.putParcelable(SAVED_INSTANCE_BITMAP, editedBitmap);
        outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
        }*/

        super.onSaveInstanceState(outState);
        }
       private void processImage(int image) {
        Bitmap bitmap = decodeBitmapImage(image);
        if (detector.isOperational() && bitmap != null) {
        editedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
        .getHeight(), bitmap.getConfig());
        float scale = getResources().getDisplayMetrics().density;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setTextSize((int) (16 * scale));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6f);
        Canvas canvas = new Canvas(editedBitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
        SparseArray<Face> faces = detector.detect(frame);
        txtSampleDesc.setText(null);

        for (int index = 0; index < faces.size(); ++index) {
        Face face = faces.valueAt(index);
        canvas.drawRect(
        face.getPosition().x,
        face.getPosition().y,
        face.getPosition().x + face.getWidth(),
        face.getPosition().y + face.getHeight(), paint);


        canvas.drawText("Face " + (index + 1), face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight(), paint);

        txtSampleDesc.setText(txtSampleDesc.getText() + "FACE " + (index + 1) + "\n");
        txtSampleDesc.setText(txtSampleDesc.getText() + "Smile probability:" + " " + face.getIsSmilingProbability() + "\n");
        txtSampleDesc.setText(txtSampleDesc.getText() + "Left Eye Is Open Probability: " + " " + face.getIsLeftEyeOpenProbability() + "\n");
        txtSampleDesc.setText(txtSampleDesc.getText() + "Right Eye Is Open Probability: " + " " + face.getIsRightEyeOpenProbability() + "\n\n");

        for (Landmark landmark : face.getLandmarks()) {
        int cx = (int) (landmark.getPosition().x);
        int cy = (int) (landmark.getPosition().y);
        canvas.drawCircle(cx, cy, 8, paint);
        }
        }

        if (faces.size() == 0) {
        txtSampleDesc.setText("Scan Failed: Found nothing to scan");
        } else {
        imageView.setImageBitmap(editedBitmap);
        txtSampleDesc.setText(txtSampleDesc.getText() + "No of Faces Detected: " + " " + String.valueOf(faces.size()));
        }
        } else {
        txtSampleDesc.setText("Could not set up the detector!");
        }
        }

private Bitmap decodeBitmapImage(int image) {
        int targetW = 300;
        int targetH = 300;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(getResources(), image,
        bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeResource(getResources(), image,
        bmOptions);
        }

private void processCameraPicture() throws Exception {
        Bitmap bitmap = decodeBitmapUri(this, imageUri);
        if (detector.isOperational() && bitmap != null) {
        editedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
        .getHeight(), bitmap.getConfig());
        float scale = getResources().getDisplayMetrics().density;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setTextSize((int) (16 * scale));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6f);
        Canvas canvas = new Canvas(editedBitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
        SparseArray<Face> faces = detector.detect(frame);
        txtTakenPicDesc.setText(null);
        for (int index = 0; index < faces.size(); ++index) {
        Face face = faces.valueAt(index);
        canvas.drawRect(
        face.getPosition().x,
        face.getPosition().y,
        face.getPosition().x + face.getWidth(),
        face.getPosition().y + face.getHeight(), paint);

        canvas.drawText("Face " + (index + 1), face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight(), paint);

        txtTakenPicDesc.setText("FACE " + (index + 1) + "\n");
        txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Smile probability:" + " " + face.getIsSmilingProbability() + "\n");
        txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Left Eye Is Open Probability: " + " " + face.getIsLeftEyeOpenProbability() + "\n");
        txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Right Eye Is Open Probability: " + " " + face.getIsRightEyeOpenProbability() + "\n\n");

        for (Landmark landmark : face.getLandmarks()) {
        int cx = (int) (landmark.getPosition().x);
        int cy = (int) (landmark.getPosition().y);
        canvas.drawCircle(cx, cy, 8, paint);
        }
        }

        if (faces.size() == 0) {
        txtTakenPicDesc.setText("Scan Failed: Found nothing to scan");
        } else {
        imgTakePicture.setImageBitmap(editedBitmap);
        txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "No of Faces Detected: " + " " + String.valueOf(faces.size()));
        }
        } else {
        txtTakenPicDesc.setText("Could not set up the detector!");
        }
        }

      private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 300;
        int targetH = 300;
        Log.d("uri",""+uri);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        bmOptions.inSampleSize = 2;
        try{
                BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        }catch (Exception e)
        {
             e.printStackTrace();
        }

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
        .openInputStream(uri), null, bmOptions);
        }

       @Override
       protected void onDestroy() {
        super.onDestroy();
        detector.release();
        }
        }
