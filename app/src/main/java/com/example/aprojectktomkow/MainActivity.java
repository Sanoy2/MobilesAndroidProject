package com.example.aprojectktomkow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aprojectktomkow.Models.Recipe;
import com.example.aprojectktomkow.Providers.ApiUrl;
import com.example.aprojectktomkow.Repositories.Token.IIdentityRepository;
import com.example.aprojectktomkow.Repositories.Token.IoC.IoC;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
{
    private final int LOGIN_RETURNED = 1;

    private final Fragment fragmentAccount = new AccountFragment();
    private final Fragment fragmentLoggedAccount = new LoggedAccountFragment();
    private final Fragment fragmentRecipes = new RecipesFragment();
    private final Fragment fragmentMyRecipes = new MyRecipesFragment();
    private final Fragment fragmentFavourites = new FavouritesFragment();
    private Fragment activeFragment = fragmentRecipes;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private List<Recipe> recipes;
    IIdentityRepository identityRepository = IoC.getIdentityRepository();

    private BottomNavigationView.OnNavigationItemSelectedListener navSelectedItemListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.navigation_account:
                    showAccountFragment();
                    break;
                case R.id.navigation_recipes:
                    fragmentManager.beginTransaction().hide(activeFragment).show(fragmentRecipes).commit();
                    activeFragment = fragmentRecipes;
                    break;
                case R.id.navigation_my_recipes:
                    fragmentManager.beginTransaction().hide(activeFragment).show(fragmentMyRecipes).commit();
                    activeFragment = fragmentMyRecipes;
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // temporary
//        Intent intent = new Intent(this, CreateRecipeActivity.class);
//        startActivity(intent);
//        // ^^^^^^^^^^^^^^^^^^

        recipes = new ArrayList<>();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navSelectedItemListener);

        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentAccount, "1").hide(fragmentAccount).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentRecipes, "2").hide(fragmentRecipes).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentMyRecipes, "3").hide(fragmentMyRecipes).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentFavourites, "4").hide(fragmentFavourites).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentLoggedAccount, "5").hide(fragmentLoggedAccount).commit();

        fragmentManager.beginTransaction().show(getAccountFragment()).commit();
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
                    firstEvent = (JSONObject) timeline.get(0);
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

    public void goToRegister(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_RETURNED);
    }

    public void createRecipe(View view)
    {
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_RETURNED && resultCode == Activity.RESULT_OK) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    showAccountFragment();
                }
            }, 50);
        }
    }

    public void getAllRecipes(View view)
    {
        if(identityRepository.isUserLogged())
        {
            getAllRecipesUserLogged();
        }
        else
        {
            getAllRecipesNonLogged();
        }

    }

    private void getAllRecipesNonLogged()
    {
        String url = ApiUrl.getRecipesUrlNonLoggedUser();
        HttpUtils.get(url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                TextView textView = findViewById(R.id.test_label);
                textView.setText("Json object instead of array");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline)
            {
                recipes.clear();
                try
                {
                    int objectsNumber = timeline.length();
                    StringBuilder builder = new StringBuilder();
                    JSONObject json;
                    String text;
                    Recipe recipe;
                    Gson gson = new Gson();
                    for (int i = 0; i < objectsNumber; i++)
                    {
                        json = (JSONObject) timeline.get(i);
                        recipe = gson.fromJson(json.toString(), Recipe.class);
                        recipes.add(recipe);

                        text = recipe.getName() + " " + recipe.getDescription();
                        builder.append(text).append("\n");
                    }

                    recipe = null;
                    TextView textView = findViewById(R.id.test_label);
                    textView.setText(builder.toString());
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getAllRecipesUserLogged()
    {
        String url = ApiUrl.getRecipesUrlNonLoggedUser();
        HttpUtils.get(url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                TextView textView = findViewById(R.id.test_label);
                textView.setText("Json object instead of array");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline)
            {
                recipes.clear();
                try
                {
                    int objectsNumber = timeline.length();
                    StringBuilder builder = new StringBuilder();
                    JSONObject json;
                    String text;
                    Recipe recipe;
                    Gson gson = new Gson();
                    for (int i = 0; i < objectsNumber; i++)
                    {
                        json = (JSONObject) timeline.get(i);
                        recipe = gson.fromJson(json.toString(), Recipe.class);
                        recipes.add(recipe);

                        text = recipe.getName() + " " + recipe.getDescription();
                        builder.append(text).append("\n");
                    }

                    recipe = null;
                    TextView textView = findViewById(R.id.test_label);
                    textView.setText(builder.toString());
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private Fragment getAccountFragment()
    {
        if(identityRepository.isUserLogged())
        {
            TextView info_username = findViewById(R.id.info_username);
            TextView info_email = findViewById(R.id.info_email);

            info_username.setText(identityRepository.getUsername());
            info_email.setText(identityRepository.getEmail());

            return fragmentLoggedAccount;
        }
        else
        {
            return fragmentAccount;
        }
    }

    private void showAccountFragment()
    {
        fragmentManager.beginTransaction().hide(activeFragment).show(getAccountFragment()).commit();
        activeFragment = getAccountFragment();
    }

    public void logout(View view)
    {
        identityRepository.logout();
        showAccountFragment();
    }
}