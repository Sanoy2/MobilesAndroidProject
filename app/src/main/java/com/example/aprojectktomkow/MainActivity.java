package com.example.aprojectktomkow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aprojectktomkow.DB.Note;
import com.example.aprojectktomkow.DB.NoteDAO;
import com.example.aprojectktomkow.Models.Recipe;
import com.example.aprojectktomkow.Models.RecipesListAdapter;
import com.example.aprojectktomkow.Providers.ApiUrl;
import com.example.aprojectktomkow.Repositories.Token.IIdentityRepository;
import com.example.aprojectktomkow.Repositories.Token.IoC.IoC;
import com.google.gson.Gson;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
{
    private final int LOGIN_RETURNED = 1;
    private final int DETAILS_RETURNED = 1;
    private final int REQUEST_SEND_DELAY = 500;

    private static final String TAG = "MainActivity";
    boolean goBackToMyRecipes = false;

    private final Fragment fragmentAccount = new AccountFragment();
    private final Fragment fragmentLoggedAccount = new LoggedAccountFragment();
    private final Fragment fragmentRecipes = new RecipesFragment();
    private final Fragment fragmentMyRecipes = new MyRecipesFragment();
    private final Fragment fragmentFavourites = new FavouritesFragment();
    private Fragment activeFragment = fragmentRecipes;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private List<Recipe> recipes;
    private List<Recipe> my_recipes;
    IIdentityRepository identityRepository = IoC.getIdentityRepository();

    private NoteDAO noteDAO;

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
                    goBackToMyRecipes = false;
                    break;
                case R.id.navigation_recipes:
                    fragmentManager.beginTransaction().hide(activeFragment).show(fragmentRecipes).commit();
                    activeFragment = fragmentRecipes;
                    goBackToMyRecipes = false;
                    break;
                case R.id.navigation_my_recipes:
                    showMyRecipesFragment();
                    goBackToMyRecipes = true;
                    break;
                case R.id.navigation_favourites:
                    fragmentManager.beginTransaction().hide(activeFragment).show(fragmentFavourites).commit();
                    activeFragment = fragmentFavourites;
                    goBackToMyRecipes = false;
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

        // INIT START
        recipes = new ArrayList<>();
        my_recipes = new ArrayList<>();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navSelectedItemListener);

        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentAccount, "1").hide(fragmentAccount).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentRecipes, "2").hide(fragmentRecipes).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentMyRecipes, "3").hide(fragmentMyRecipes).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentFavourites, "4").hide(fragmentFavourites).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentLoggedAccount, "5").hide(fragmentLoggedAccount).commit();

        fragmentManager.beginTransaction().show(getAccountFragment()).commit();

        // INIT END

        noteDAO = new NoteDAO(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == LOGIN_RETURNED && resultCode == Activity.RESULT_OK)
        {
            Handler handler = new Handler();
            if(goBackToMyRecipes)
            {
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showMyRecipesFragment();
                    }
                }, 50);
            }
            else
            {
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
    }


    private Fragment getAccountFragment()
    {
        if (identityRepository.isUserLogged())
        {
            TextView info_username = findViewById(R.id.info_username);
            TextView info_email = findViewById(R.id.info_email);

            info_username.setText(identityRepository.getUsername());
            info_email.setText(identityRepository.getEmail());

            return fragmentLoggedAccount;
        } else
        {
            return fragmentAccount;
        }
    }

    private void showAccountFragment()
    {
        fragmentManager.beginTransaction().hide(activeFragment).show(getAccountFragment()).commit();
        activeFragment = getAccountFragment();
    }

    private void showMyRecipesFragment()
    {
        if (identityRepository.isUserLogged())
        {
            fragmentManager.beginTransaction().hide(activeFragment).show(fragmentMyRecipes).commit();
            activeFragment = fragmentMyRecipes;
        } else
        {
            showAccountFragment();
        }
    }

    public void logout(View view)
    {
        identityRepository.logout();
        showAccountFragment();
    }

    // RECIPES
    private void recipesActivateLoadingScreen()
    {
        recipesHideError();
        recipesDeactivateButtons();
        recipesShowProgressCircle();
    }

    private void recipesDeactivateLoadingScreen()
    {
        recipesActivateButtons();
        resipesHideProgressCircle();
    }

    private void recipesActivateButtons()
    {
        findViewById(R.id.recipes_fire).setEnabled(true);
    }

    private void recipesDeactivateButtons()
    {
        findViewById(R.id.recipes_fire).setEnabled(false);
    }

    private void resipesHideProgressCircle()
    {
        recipesGetProgressCircle().setVisibility(View.GONE);
    }

    private void recipesShowProgressCircle()
    {
        recipesGetProgressCircle().setVisibility(View.VISIBLE);
    }

    private void recipesShowError(String error)
    {
        if (error != null && error.length() > 0)
        {
            TextView errorMessage = findViewById(R.id.recipes_error_message);
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText(error);
        }
    }

    private void recipesHideError()
    {
        TextView errorMessage = findViewById(R.id.recipes_error_message);
        errorMessage.setVisibility(View.GONE);
    }

    private ProgressBar recipesGetProgressCircle()
    {
        ProgressBar progressCircle = findViewById(R.id.recipes_progress_circle);
        return progressCircle;
    }

    public void getAllRecipes(View view)
    {
        recipesHideError();
        recipesActivateLoadingScreen();
        recipesClearContent();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (identityRepository.isUserLogged())
                {
                    getAllRecipesUserLogged();
                } else
                {
                    getAllRecipesNonLogged();
                }
            }
        }, REQUEST_SEND_DELAY);
    }

    private void getAllRecipesNonLogged()
    {
        String url = ApiUrl.getRecipesUrlNonLoggedUser();
        HttpUtils.get(url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                recipesShowError(throwable.getMessage());
                recipesDeactivateLoadingScreen();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                recipesShowError("Json object instead of array");
                recipesDeactivateLoadingScreen();
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
                    recipesShowContent();
                    recipe = null;

                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally
                {
                    recipesDeactivateLoadingScreen();
                }
            }
        });
    }

    private void getAllRecipesUserLogged()
    {
        String url = ApiUrl.getRecipesUrlLoggedUser();
        HttpUtils.attachToken(identityRepository.getToken());
        HttpUtils.get(url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                recipesShowError(throwable.getMessage());
                recipesDeactivateLoadingScreen();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                recipesShowError("Json object instead of array");
                recipesDeactivateLoadingScreen();
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
                    recipesShowContent();
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    recipesShowError("Exception: " + e.getMessage());
                } finally
                {
                    recipesDeactivateLoadingScreen();
                }
            }
        });
    }

    private void recipesShowContent()
    {
        recipesGetGridView().setAdapter(recipesGetAdapter());
        recipesGetGridView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Recipe recipe = recipes.get((int) id);
                goToDetails(recipe);
            }
        });
    }

    private void goToDetails(Recipe recipe)
    {
        Intent intent = new Intent(this, DetailsActivity.class);

        Bundle b = new Bundle();
        b.putString("name", recipe.getName());
        b.putString("description", recipe.getDescription());
        b.putString("shortDescription", recipe.getShortDescription());
        b.putString("imageUrl", recipe.getMainImageUrl());
        b.putInt("neededTime", recipe.getNeededTimeMinutes());
        b.putString("lastModification", recipe.getDateOfLastModification().toString());
        b.putBoolean("isPrivate", recipe.isPrivate());

        intent.putExtras(b);

        startActivityForResult(intent, DETAILS_RETURNED);
    }

    private void recipesClearContent()
    {
        recipesGetGridView().setAdapter(null);
    }

    private GridView recipesGetGridView()
    {
        return findViewById(R.id.recipes_grid_view);
    }

    private RecipesListAdapter recipesGetAdapter()
    {
        RecipesListAdapter adapter = new RecipesListAdapter(this, R.layout.recipe_adapter_layout, recipes);
        return adapter;
    }

    // My recipes

    private void my_recipesActivateLoadingScreen()
    {
        my_recipesHideError();
        my_recipesDeactivateButtons();
        my_recipesShowProgressCircle();
    }

    private void my_recipesDeactivateLoadingScreen()
    {
        my_recipesActivateButtons();
        my_resipesHideProgressCircle();
    }

    private void my_recipesActivateButtons()
    {
        findViewById(R.id.my_recipes_fire).setEnabled(true);
    }

    private void my_recipesDeactivateButtons()
    {
        findViewById(R.id.my_recipes_fire).setEnabled(false);
    }

    private void my_resipesHideProgressCircle()
    {
        my_recipesGetProgressCircle().setVisibility(View.GONE);
    }

    private void my_recipesShowProgressCircle()
    {
        my_recipesGetProgressCircle().setVisibility(View.VISIBLE);
    }

    private void my_recipesShowError(String error)
    {
        if (error != null && error.length() > 0)
        {
            TextView errorMessage = findViewById(R.id.my_recipes_error_message);
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText(error);
        }
    }

    private void my_recipesHideError()
    {
        TextView errorMessage = findViewById(R.id.my_recipes_error_message);
        errorMessage.setVisibility(View.GONE);
    }

    private ProgressBar my_recipesGetProgressCircle()
    {
        ProgressBar progressCircle = findViewById(R.id.my_recipes_progress_circle);
        return progressCircle;
    }

    public void my_getAllRecipes(View view)
    {
        my_recipesHideError();
        my_recipesActivateLoadingScreen();
        my_recipesClearContent();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (identityRepository.isUserLogged())
                {
                    my_getAllRecipesUserLogged();
                } else
                {
                    my_recipesShowError("You must be logged in");
                }
            }
        }, REQUEST_SEND_DELAY);
    }

    private void my_getAllRecipesUserLogged()
    {
        String url = ApiUrl.getRecipesUrlOnlyMyRecipes();
        HttpUtils.attachToken(identityRepository.getToken());
        HttpUtils.get(url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                my_recipesShowError(throwable.getMessage());
                my_recipesDeactivateLoadingScreen();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                my_recipesShowError("Json object instead of array");
                my_recipesDeactivateLoadingScreen();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline)
            {
                my_recipes.clear();
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
                        my_recipes.add(recipe);

                        text = recipe.getName() + " " + recipe.getDescription();
                        builder.append(text).append("\n");
                    }
//                    Toast.makeText(getApplicationContext(), String.valueOf(objectsNumber), Toast.LENGTH_LONG).show();
                    recipe = null;
                    my_recipesShowContent();
                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    my_recipesShowError("Exception: " + e.getMessage());
                } finally
                {
                    my_recipesDeactivateLoadingScreen();
                }
            }
        });
    }

    private void my_recipesShowContent()
    {
        my_recipesGetListView().setAdapter(my_recipesGetAdapter());
        my_recipesGetListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Recipe recipe = my_recipes.get((int) id);
                goToDetails(recipe);
            }
        });
    }

    private void my_recipesClearContent()
    {
        my_recipesGetListView().setAdapter(null);
    }

    // Recipes adapter
    private ListView my_recipesGetListView()
    {
        return findViewById(R.id.my_recipes_list_view);
    }

    private RecipesListAdapter my_recipesGetAdapter()
    {
        RecipesListAdapter adapter = new RecipesListAdapter(this, R.layout.recipe_adapter_layout, my_recipes);
        return adapter;
    }

    // Favourites
    public void showNotes(View view)
    {
        List<Note> allNotes = noteDAO.getAllNotes();

        StringBuilder sb = new StringBuilder();

        for (Note note: allNotes)
        {
            sb.append(note.toString());
            sb.append("\n");
        }

        TextView tv = findViewById(R.id.notes_content);
        tv.setText(sb.toString());
    }

    public void addNote(View view)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        Random rand = new Random();
        int id = rand.nextInt(100000);

        Note note = new Note();
        note.setNoteText(date.toString());
        note.setId(id);

        noteDAO.insertNote(note);
    }
}