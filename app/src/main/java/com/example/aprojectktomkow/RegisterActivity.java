package com.example.aprojectktomkow;

import android.app.Activity;
import android.content.Context;
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

import com.example.aprojectktomkow.Encryption.Encrypter;
import com.example.aprojectktomkow.Encryption.IEncrypter;
import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.Registration.RegistrationCommand;
import com.example.aprojectktomkow.Models.Forms.Registration.RegistrationForm;
import com.example.aprojectktomkow.Providers.ApiUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisterActivity extends AppCompatActivity
{
    private final int REQUEST_SEND_DELAY = 750;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        hideError();
        hideProgressCircle();
        setInitialValues();
    }

    public void register(View view)
    {
        hideError();
        hideKeyboard(this);
        showProgressCircle();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                register();
            }
        }, REQUEST_SEND_DELAY);
    }

    private void register()
    {
        RegistrationForm registerForm = new RegistrationForm();
        registerForm.setUsername(getUsername());
        registerForm.setEmail(getEmail());
        registerForm.setPassword(getPassword());
        registerForm.setRepeatedPassword(getRepeatedPassword());

        IValidatorResult formValidationResult = registerForm.Validate();
        if (formValidationResult.isValid())
        {
            RegistrationCommand registerCommand = new RegistrationCommand(registerForm);
            sendCommand(registerCommand);
        } else
        {
            hideProgressCircle();
            showError(formValidationResult.errorMessage());
        }
    }

    private void sendCommand(RegistrationCommand command)
    {
        String toShow = "";
        String url = ApiUrl.getUserRegisterUrl();

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
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                hideProgressCircle();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString)
            {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                hideProgressCircle();
            }
        });
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

    private String getUsername()
    {
        EditText usernameEditText = findViewById(R.id.username);
        return usernameEditText.getText().toString();
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

    private String getRepeatedPassword()
    {
        EditText passwordRepeatEditText = findViewById(R.id.password_repeat);
        return passwordRepeatEditText.getText().toString();
    }

    private static void hideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
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
        EditText editText = findViewById(R.id.username);
        editText.setText("JohnSnow");
        editText = findViewById(R.id.email);
        editText.setText("myvalidemail@email.com");
        editText = findViewById(R.id.password);
        editText.setText("zaq12wsx");
        editText = findViewById(R.id.password_repeat);
        editText.setText("zaq12wsx");
    }
}
