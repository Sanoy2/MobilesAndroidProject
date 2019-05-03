package com.example.aprojectktomkow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateRecipeActivity extends AppCompatActivity
{
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int USE_CAMERA_RESP = 2;

    String currentPhotoPath;

    private LinearLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        bottomLayout = findViewById(R.id.bottom_layout);

        KeyboardVisibilityEvent.setEventListener(
                CreateRecipeActivity.this,
                new KeyboardVisibilityEventListener()
                {
                    @Override
                    public void onVisibilityChanged(boolean isOpen)
                    {
                        // some code depending on keyboard visiblity status
                        if (isOpen)
                        {
                            hideBottomLayout();
                        } else
                        {
                            showBottomLayout();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case USE_CAMERA_RESP:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    dispatchTakePictureIntent();
                    return;
                }
        }
    }

    public void takePicture(View view)
    {
        if (checkIfPermissionGranted(Manifest.permission.CAMERA))
        {
            dispatchTakePictureIntent();
        } else
        {
            askForPermission(Manifest.permission.CAMERA, USE_CAMERA_RESP);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView image = findViewById(R.id.picture);
            image.setImageBitmap(imageBitmap);
        } else
        {
            showToast("Something fucked up");
            Log.i("camera", "request code: " + requestCode);
            Log.i("camera", "result code: " + resultCode);
            Log.i("camera", "result code 'ok' is: " + RESULT_OK);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                showToast("IO Exception");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean checkIfPermissionGranted(String permission)
    {
        return ContextCompat.checkSelfPermission(CreateRecipeActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void askForPermission(String permission, int responseCode)
    {
        ActivityCompat.requestPermissions(CreateRecipeActivity.this, new String[]{permission}, responseCode);
    }

    private void showBottomLayout()
    {
        bottomLayout.setVisibility(View.VISIBLE);
    }

    private void hideBottomLayout()
    {
        bottomLayout.setVisibility(View.GONE);
    }

    private void showToast(String content)
    {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
    }
}
