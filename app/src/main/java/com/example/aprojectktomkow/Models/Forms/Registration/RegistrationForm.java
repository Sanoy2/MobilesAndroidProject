package com.example.aprojectktomkow.Models.Forms.Registration;

import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.Registration.RegisterValidationResult;

public class RegistrationForm
{
    String username;
    String email;
    String password;
    String repeatedPassword;

    public RegistrationForm() {}

    public IValidatorResult Validate()
    {
        return RegisterValidationResult.createInvalidResult("error");
    }

    private boolean validateUsername()
    {
        return false;
    }

    private boolean validateEmail()
    {
        return false;
    }

    private boolean validatePassword()
    {
        return false;
    }

    private boolean validatePasswordPair()
    {
        return false;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRepeatedPassword()
    {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword)
    {
        this.repeatedPassword = repeatedPassword;
    }
}
