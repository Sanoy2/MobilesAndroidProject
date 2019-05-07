package com.example.aprojectktomkow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        }
    }

    private void setIsPrivate(boolean isPrivate)
    {

    }

    private void setImageUrl(String imageUrl)
    {

    }

    private void setName(String value)
    {

    }

    private void setDescription(String value)
    {

    }

    private void setShortDescription(String value)
    {

    }

    private void setLastDateModification(String value)
    {

    }

    private void setNeededTime(int value)
    {

    }
}
