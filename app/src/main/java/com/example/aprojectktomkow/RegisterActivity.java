package com.example.aprojectktomkow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aprojectktomkow.Encryption.Encrypter;
import com.example.aprojectktomkow.Encryption.IEncrypter;

public class RegisterActivity extends AppCompatActivity
{

    IEncrypter encrypter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        encrypter = new Encrypter();
    }


    public void testRun(View view)
    {

        EditText editText = findViewById(R.id.password);
        String text = editText.getText().toString();
        String hash = encrypter.sha256(text);
        Toast.makeText(getApplicationContext(), hash, Toast.LENGTH_LONG).show();
    }
}
