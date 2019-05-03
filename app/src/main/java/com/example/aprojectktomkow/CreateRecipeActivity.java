package com.example.aprojectktomkow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateRecipeActivity extends AppCompatActivity
{
    static final int REQUEST_IMAGE_GET = 1;

    private static final int READ_EXTERNAL_STORAGE_RESP = 2;

    String currentPhotoPath;

    private LinearLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        bottomLayout = findViewById(R.id.bottom_layout);

        deactivateLoadingScreen();
        hideError();

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
            case READ_EXTERNAL_STORAGE_RESP:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    dispatchTakePictureIntent();
                    return;
                }
        }
    }

    public void takePicture(View view)
    {
        if (checkIfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            dispatchTakePictureIntent();
        } else
        {
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_RESP);
        }

    }

    public void dispatchTakePictureIntent()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GET);
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_IMAGE_GET) {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in ImageView
//                    ImageView((ImageView) findViewById(R.id.imgView)).setImageURI(selectedImageUri);
                    ImageView iv = findViewById(R.id.picture);
                    iv.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
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

    private void activateLoadingScreen()
    {
        hideError();
        deactivateButtons();
        showProgressCircle();
    }

    private void deactivateLoadingScreen()
    {
        activateButtons();
        hideProgressCircle();
    }

    private void activateButtons()
    {
        findViewById(R.id.create_recipe_button).setEnabled(true);
        findViewById(R.id.button_to_take_picture).setEnabled(true);
    }

    private void deactivateButtons()
    {
        findViewById(R.id.create_recipe_button).setEnabled(false);
        findViewById(R.id.button_to_take_picture).setEnabled(false);
    }

    private void hideProgressCircle()
    {
        getProgessCircle().setVisibility(View.GONE);
    }

    private void showProgressCircle()
    {
        getProgessCircle().setVisibility(View.VISIBLE);
    }

    private void showError(String error)
    {
        if (error != null && error.length() > 0)
        {
            TextView errorMessage = findViewById(R.id.error_message);
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText(error);
        }
    }

    private void hideError()
    {
        TextView errorMessage = findViewById(R.id.error_message);
        errorMessage.setVisibility(View.GONE);
    }

    private ProgressBar getProgessCircle()
    {
        ProgressBar progressCircle = findViewById(R.id.progress_circle);
        return progressCircle;
    }

    private void showToast(String content)
    {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
    }
}
