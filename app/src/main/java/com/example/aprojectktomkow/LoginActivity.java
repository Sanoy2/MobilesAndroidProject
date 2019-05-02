package com.example.aprojectktomkow;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.Login.LoginCommand;
import com.example.aprojectktomkow.Models.Forms.Login.LoginForm;
import com.example.aprojectktomkow.Models.Forms.Registration.RegistrationCommand;
import com.example.aprojectktomkow.Providers.ApiUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity
{
    private final int REGISTRATION = 1;
    private final int REQUEST_SEND_DELAY = 750;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideError();
        hideProgressCircle();
        setInitialValues();
    }

    public void goToRegister(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTRATION);
    }

    public void login(View view)
    {
        showProgressCircle();
        hideError();
        hideKeyboard(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                login();
            }
        }, REQUEST_SEND_DELAY);
    }

    private void login()
    {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail(getEmail());
        loginForm.setPassword(getPassword());

        IValidatorResult formValidationResult = loginForm.Validate();
        if (formValidationResult.isValid())
        {
            LoginCommand loginCommand = new LoginCommand(loginForm);
            sendCommand(loginCommand);
        }
        else
        {
            showError(formValidationResult.errorMessage());
            hideProgressCircle();
        }
    }


    private void sendCommand(LoginCommand command)
    {
        String url = ApiUrl.getUserLoginUlr();

        JSONObject jsonParams = new JSONObject(command.toHashMap());
        StringEntity entity = null;
        try
        {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getApplicationContext(), url, entity, "application/json", new TextHttpResponseHandler()
        {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
                hideProgressCircle();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString)
            {
                Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
                hideProgressCircle();
            }
        });
    }

    private String getEmail()
    {
        EditText emailEditText = findViewById(R.id.email);
        return emailEditText.getText().toString();
    }

    private String getPassword()
    {
        EditText passwordEditText = findViewById(R.id.password);
        return passwordEditText.getText().toString();
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

    private static void hideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
        {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void hideProgressCircle()
    {
        getProgessCircle().setVisibility(View.GONE);
    }

    private void showProgressCircle()
    {
        getProgessCircle().setVisibility(View.VISIBLE);
    }

    private ProgressBar getProgessCircle()
    {
        ProgressBar progressCircle = findViewById(R.id.progress_circle);
        return progressCircle;
    }

    @Override
    public void finish()
    {
        Intent intent = new Intent();

        intent.putExtra("result", "some result body");

        setResult(RESULT_OK, intent);
        super.finish();
    }

    // temporary method
    private void setInitialValues()
    {
        EditText editText = findViewById(R.id.email);
        editText.setText("jd@em.com");
        editText = findViewById(R.id.password);
        editText.setText("password");
    }
}
