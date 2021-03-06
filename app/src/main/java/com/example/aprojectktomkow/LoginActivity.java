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
import com.example.aprojectktomkow.Providers.ApiUrl;
import com.example.aprojectktomkow.Repositories.Token.IIdentityRepository;
import com.example.aprojectktomkow.Repositories.Token.IoC.IoC;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity
{
    private final int REGISTRATION_RETURN = 1;
    private final int REQUEST_SEND_DELAY = 600;
    private final int FINISH_DELAY = 250;

    private IIdentityRepository identityRepository = IoC.getIdentityRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideError();
        hideMessage();
        deactivateLoadingScreen();
        setInitialValues();
    }

    public void goToRegister(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTRATION_RETURN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideError();
        if (requestCode == REGISTRATION_RETURN && resultCode == Activity.RESULT_OK)
        {
            Bundle extras = data.getExtras();
            if (extras != null) {
                String message = extras.getString("result");
                String email = extras.getString("email");
                String password = extras.getString("password");

                if(email != null && !email.isEmpty())
                {
                    if(password != null && !password.isEmpty())
                    {
                        setInitialValues(email, password);
                    }
                }

                if(message != null && !message.isEmpty())
                {
                    showMessage(message);
                }
            }
        }
    }

    public void login(View view)
    {
        activateLoadingScreen();
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
            deactivateLoadingScreen();
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
        HttpUtils.post(getApplicationContext(), url, entity, "application/json", new JsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                showError(responseString);
                deactivateLoadingScreen();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonToken)
            {
                try
                {
                    String token = jsonToken.getString("token");
                    String username = jsonToken.getString("username");
                    String email = jsonToken.getString("email");
                    identityRepository.login(token, username, email);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            finish();
                        }
                    }, FINISH_DELAY);
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                deactivateLoadingScreen();
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

    private void showMessage(String message)
    {
        if (message != null && message.length() > 0)
        {
            TextView justMessage = findViewById(R.id.just_message);
            justMessage.setVisibility(View.VISIBLE);
            justMessage.setText(message);
        }
    }

    private void hideMessage()
    {
        TextView justMessage = findViewById(R.id.just_message);
        justMessage.setVisibility(View.GONE);
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

    private void activateLoadingScreen()
    {
        hideError();
        hideMessage();
        deactivateButtons();
        deactivateInputs();
        showProgressCircle();
    }

    private void deactivateLoadingScreen()
    {
        activateButtons();
        activateInputs();
        hideProgressCircle();
    }

    private void activateButtons()
    {
        findViewById(R.id.login_sign_in_button).setEnabled(true);
        findViewById(R.id.login_register_button).setEnabled(true);
    }

    private void deactivateButtons()
    {
        findViewById(R.id.login_sign_in_button).setEnabled(false);
        findViewById(R.id.login_register_button).setEnabled(false);
    }

    private void activateInputs()
    {
        findViewById(R.id.email).setEnabled(true);
        findViewById(R.id.password).setEnabled(true);
    }

    private void deactivateInputs()
    {
        findViewById(R.id.email).setEnabled(false);
        findViewById(R.id.password).setEnabled(false);
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

    private void setInitialValues(String email, String password)
    {
        EditText editText = findViewById(R.id.email);
        editText.setText(email);
        editText = findViewById(R.id.password);
        editText.setText(password);
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
