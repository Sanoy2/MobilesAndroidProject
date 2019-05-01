package com.example.aprojectktomkow.Models.Forms.Login;

import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class LoginForm
{
    private final int MIN_PASSWORD_LENGTH = 6;

    private String email;
    private String password;

    public IValidatorResult Validate()
    {
        List<IValidatorResult> validationResults = new ArrayList<>();
        validationResults.add(validateEmail());
        validationResults.add(validatePassword());

        return ValidationResult.createResult(validationResults);
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
}
