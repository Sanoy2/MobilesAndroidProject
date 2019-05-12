package com.example.aprojectktomkow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.Recipe.NewRecipeCommand;
import com.example.aprojectktomkow.Models.Forms.Recipe.NewRecipeForm;
import com.example.aprojectktomkow.Providers.ApiUrl;
import com.example.aprojectktomkow.Providers.ImageCompressor;
import com.example.aprojectktomkow.Repositories.Token.IIdentityRepository;
import com.example.aprojectktomkow.Repositories.Token.IoC.IoC;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CreateRecipeActivity extends AppCompatActivity
{
    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_SEND_DELAY = 500;

    File selectedFile = null;

    private static final int READ_EXTERNAL_STORAGE_RESP = 2;

    IIdentityRepository identityRepository = IoC.getIdentityRepository();

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

    public String getPathFromURI(Uri contentUri)
    {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst())
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if (resultCode == RESULT_OK)
            {
                if (requestCode == REQUEST_IMAGE_GET)
                {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    currentPhotoPath = path;
                    if (path != null)
                    {
                        File f = new File(path);
                        selectedFile = f;
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in ImageView
//                    ImageView((ImageView) findViewById(R.id.imgView)).setImageURI(selectedImageUri);
                    ImageView iv = findViewById(R.id.picture);
                    iv.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e)
        {
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

    public void sendNewRecipe(View view)
    {
        activateLoadingScreen();
        deactivateButtons();
        hideError();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                sendNewRecipe();
            }
        }, REQUEST_SEND_DELAY);
    }

    private void sendNewRecipe()
    {
        NewRecipeForm form = getRecipeForm();

        IValidatorResult formValidationResult = form.Validate();
        if (identityRepository.isUserLogged())
        {
            if (formValidationResult.isValid())
            {
                NewRecipeCommand command = new NewRecipeCommand(form);
                sendNewRecipeCommand(command);
            } else
            {
                deactivateLoadingScreen();
                showError(formValidationResult.errorMessage());
            }
        } else
        {
            showError("User must be logged in to send recipe");
            deactivateLoadingScreen();
            activateButtons();
        }
    }

    public void sendImage(View view)
    {
        sendImage();
    }

    private void sendImage()
    {
        String url = ApiUrl.getImagesUrlCreate();
        Uri selectedImageUri = Uri.fromFile(selectedFile);
        RequestParams params = new RequestParams();

        ImageCompressor compressor = new ImageCompressor();

        File uncompressedImage = new File(currentPhotoPath);
        File compressedImage = compressor.Compress(uncompressedImage);

        try
        {
            params.put("file", compressedImage, "image/jpeg");
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        HttpUtils.attachToken(identityRepository.getToken());
        HttpUtils.post(url, params, new TextHttpResponseHandler()
        {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
//                showError(throwable.getMessage());
//                deactivateLoadingScreen();
//                activateButtons();
                Log.i("image", responseString);
                Log.i("image", throwable.getMessage());
                Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString)
            {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "Recipe successfully created :)", Toast.LENGTH_LONG).show();
//                deactivateLoadingScreen();
//                activateButtons();
//                finish();
            }
        });
    }

    private void sendNewRecipeCommand(NewRecipeCommand command)
    {
        String url = ApiUrl.getRecipesUrlCreate();
        HttpUtils.attachToken(identityRepository.getToken());

        final JSONObject jsonParams = new JSONObject(command.toHashMap());
        StringEntity entity = null;
        try
        {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        HttpUtils.attachToken(identityRepository.getToken());
        HttpUtils.post(getApplicationContext(), url, entity, "application/json", new TextHttpResponseHandler()
        {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                showError(throwable.getMessage());
                deactivateLoadingScreen();
                activateButtons();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString)
            {
                Toast.makeText(getApplicationContext(), "Recipe successfully created :)", Toast.LENGTH_LONG).show();
                deactivateLoadingScreen();
                activateButtons();
                finish();
            }
        });
    }

    private NewRecipeForm getRecipeForm()
    {
        NewRecipeForm form = new NewRecipeForm();
        form.setName(getName());
        form.setShortDescription(getShortDescription());
        form.setDescription(getDescription());
        form.setNeededTimeMinutes(getNeededTime());
        form.setImageUrl(getImageUrl());
        form.setPrivate(getIsPrivate());

        return form;
    }

    private String getNeededTime()
    {
        EditText editText = findViewById(R.id.recipe_time);
        return editText.getText().toString();
    }

    private String getName()
    {
        EditText editText = findViewById(R.id.recipe_title);
        return editText.getText().toString();
    }

    private String getShortDescription()
    {
        EditText editText = findViewById(R.id.recipe_short_description);
        return editText.getText().toString();
    }

    private String getDescription()
    {
        EditText editText = findViewById(R.id.recipe_description);
        return editText.getText().toString();
    }

    private String getImageUrl()
    {
        return "none";
    }

    private boolean getIsPrivate()
    {
        CheckBox checkBox = findViewById(R.id.private_recipe);
        return checkBox.isChecked();
    }

    @Override
    public void finish()
    {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
