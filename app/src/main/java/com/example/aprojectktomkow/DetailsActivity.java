package com.example.aprojectktomkow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aprojectktomkow.Providers.ApiUrl;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class DetailsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            setName(extras.getString("name"));
            setDescription(extras.getString("description"));
            setShortDescription(extras.getString("shortDescription"));
            setImageUrl(extras.getString("imageUrl"));
            setNeededTime(extras.getInt("neededTime"));
            setLastDateModification(extras.getString("lastModification"));
            setIsPrivate(extras.getBoolean("isPrivate"));
            setShortDescription(extras.getString("shortDescription"));
        }
    }

    private void setIsPrivate(boolean isPrivate)
    {
        String message = "";
        if(isPrivate)
        {
            message = "Private recipe";
        }
        else
        {
            message = "Public recipe";
        }

        TextView textView = findViewById(R.id.recipe_detail_is_public);
        textView.setText(message);
    }

    private void setImageUrl(String imageUrl)
    {
       if(imageUrl != null && !imageUrl.isEmpty())
       {
           setImage(imageUrl);
       }
       else
       {
           return;
       }
    }

    private void setName(String value)
    {
        TextView textView = findViewById(R.id.recipe_detail_name);
        textView.setText(value);
    }

    private void setDescription(String value)
    {
        TextView textView = findViewById(R.id.recipe_detail_description);
        textView.setText(value);
    }

    private void setShortDescription(String value)
    {
        TextView textView = findViewById(R.id.recipe_detail_short_description);
        textView.setText(value);
    }

    private void setLastDateModification(String value)
    {
        TextView textView = findViewById(R.id.recipe_detail_last_modification);
        String message = "Last modification on: " + value;
        textView.setText(message);
    }

    private void setNeededTime(int value)
    {
        TextView textView = findViewById(R.id.recipe_detail_time);
        String message = "You need: " + String.valueOf(value) + " minutes";
        textView.setText(message);
    }

    private void setImage(String imageId)
    {
        String url = ApiUrl.getImagesUrlCreate() + imageId;

        HttpUtils.get(url, null, new FileAsyncHttpResponseHandler(getApplicationContext())
        {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file)
            {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response)
            {
                Bitmap bMap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ImageView image = findViewById(R.id.recipe_detail_image);
                image.setImageBitmap(bMap);
            }
        });
    }
}
