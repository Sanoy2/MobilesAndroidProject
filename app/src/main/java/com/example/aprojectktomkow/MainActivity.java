package com.example.aprojectktomkow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.aprojectktomkow.Providers.ApiUrl;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private final Fragment fragmentAccount = new AccountFragment();
    private final Fragment fragmentRecipes = new RecipesFragment();
    private final Fragment fragmentMyRecipes = new MyRecipesFragment();
    private final Fragment fragmentFavourites = new FavouritesFragment();
    private Fragment activeFragment = fragmentRecipes;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener navSelectedItemListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.navigation_account:
                    fragmentManager.beginTransaction().hide(activeFragment).show(fragmentAccount).commit();
                    activeFragment = fragmentAccount;
                    break;
                case R.id.navigation_recipes:
                    fragmentManager.beginTransaction().hide(activeFragment).show(fragmentRecipes).commit();
                    activeFragment = fragmentRecipes;
                    break;
                case R.id.navigation_my_recipes:
                    fragmentManager.beginTransaction().hide(activeFragment).show(fragmentMyRecipes).commit();
                    activeFragment =  fragmentMyRecipes;
                    break;
                case R.id.navigation_favourites:
                    fragmentManager.beginTransaction().hide(activeFragment).show(fragmentFavourites).commit();
                    activeFragment = fragmentFavourites;
                    break;
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navSelectedItemListener);

        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentAccount, "1").commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentRecipes, "2").hide(fragmentRecipes).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentMyRecipes, "3").hide(fragmentMyRecipes).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentFavourites, "4").hide(fragmentFavourites).commit();
    }

    public void increment(View view)
    {
        TextView testTextView = findViewById(R.id.test_label);
        testTextView.setText("clicked");

        String url = ApiUrl.getUsersUrl();
        HttpUtils.get(url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline)
            {
                JSONObject firstEvent = null;
                try
                {
                    firstEvent = (JSONObject)timeline.get(0);
                    TextView testTextView = findViewById(R.id.test_label);
                    testTextView.setText(firstEvent.toString());
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                    TextView testTextView = findViewById(R.id.test_label);
                    testTextView.setText("Connection error");
                }
            }
        });
    }
}
