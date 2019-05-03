package com.example.aprojectktomkow;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class CreateRecipeActivity extends AppCompatActivity
{
    private LinearLayout bottomLayout;

    public void showToast(String content)
    {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
    }

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

    private void showBottomLayout()
    {
        bottomLayout.setVisibility(View.VISIBLE);
    }

    private void hideBottomLayout()
    {
        bottomLayout.setVisibility(View.GONE);
    }


}
