package com.example.aprojectktomkow.Models.Forms.Registration;

import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class RegistrationForm
{
    private final int MIN_USERNAME_LENGTH = 4;
    private final int MIN_PASSWORD_LENGTH = 6;

    private String username;
    private String email;
    private String password;
    private String repeatedPassword;

    public IValidatorResult Validate()
    {
        List<IValidatorResult> validationResults = new ArrayList<>();
        validationResults.add(validateUsername());
        validationResults.add(validateEmail());
        validationResults.add(validatePassword());
        validationResults.add(validatePasswordPair());

        return ValidationResult.createResult(validationResults);
    }

    private IValidatorResult validateUsername()
    {
        if(username.trim().length() >= MIN_USERNAME_LENGTH)
        {
            return ValidationResult.createValidResult();
        }
        return ValidationResult.createInvalidResult("Username should contain at least " + MIN_PASSWORD_LENGTH + " characters\n");
    }

    private IValidatorResult validateEmail()
    {
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            return ValidationResult.createValidResult();
        }
        return ValidationResult.createInvalidResult("Incorrect email address\n");
    }

    private IValidatorResult validatePassword()
    {
        if(password.length() >= MIN_PASSWORD_LENGTH)
        {
            return ValidationResult.createValidResult();
        }
        return ValidationResult.createInvalidResult("Password should contain at least " + MIN_PASSWORD_LENGTH + " characters\n");
    }

    private IValidatorResult validatePasswordPair()
    {
        if(password.equals(repeatedPassword))
        {
            return ValidationResult.createValidResult();
        }
        return ValidationResult.createInvalidResult("Passwords are not the same\n");
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username.trim();
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email.toLowerCase().trim();
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password.trim();
    }

    public String getRepeatedPassword()
    {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword)
    {
        this.repeatedPassword = repeatedPassword.trim();
    }
}
