package com.example.aprojectktomkow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aprojectktomkow.Encryption.Encrypter;
import com.example.aprojectktomkow.Encryption.IEncrypter;
import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.Registration.RegistrationForm;

public class RegisterActivity extends AppCompatActivity
{
    IEncrypter encrypter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        encrypter = new Encrypter();
        hideError();
    }

    public void register(View view)
    {
        RegistrationForm form = new RegistrationForm();
        form.setUsername(getUsername());
        form.setEmail(getEmail());
        form.setPassword(getPassword());
        form.setRepeatedPassword(getRepeatedPassword());

        IValidatorResult formValidationResult = form.Validate();
        if(!formValidationResult.isValid())
        {
            showError(formValidationResult.errorMessage());
        }
    }

    private void showError(String error)
    {
        if(error != null && error.length() > 0)
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
        EditText password_repeatEditText = findViewById(R.id.password_repeat);
        return password_repeatEditText.getText().toString();
    }

    public void testRun(View view)
    {

        EditText editText = findViewById(R.id.password);
        String text = editText.getText().toString();
        String hash = encrypter.sha256(text);
        Toast.makeText(getApplicationContext(), hash, Toast.LENGTH_LONG).show();
    }
}
