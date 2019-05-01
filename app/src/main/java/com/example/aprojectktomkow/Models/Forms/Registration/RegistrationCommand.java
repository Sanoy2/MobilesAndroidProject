package com.example.aprojectktomkow.Models.Forms.Registration;

import com.example.aprojectktomkow.Encryption.Encrypter;
import com.example.aprojectktomkow.Encryption.IEncrypter;
import com.example.aprojectktomkow.Models.Forms.IValidatorResult;

import java.util.HashMap;

public class RegistrationCommand
{
    String username;
    String email;
    String password;

    public RegistrationCommand(RegistrationForm form)
    {
        username = form.getUsername();
        email = form.getEmail();
        IEncrypter encrypter = new Encrypter();
        password = encrypter.sha256(form.getPassword());
    }

    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("username", username);
        map.put("password", password);
        return map;
    }
}
