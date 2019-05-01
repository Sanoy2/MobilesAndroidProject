package com.example.aprojectktomkow.Models.Forms.Login;

import com.example.aprojectktomkow.Encryption.Encrypter;
import com.example.aprojectktomkow.Encryption.IEncrypter;
import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.Registration.RegistrationForm;
import com.example.aprojectktomkow.Models.Forms.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class LoginCommand
{
    private String email;
    private String password;

    public LoginCommand(LoginForm form) throws Exception
    {
        IValidatorResult formValidationResult = form.Validate();
        if(formValidationResult.isValid())
        {
            email = form.getEmail();
            IEncrypter encrypter = new Encrypter();
            password = encrypter.sha256(form.getPassword());
        }
        else
        {
            throw new Exception(formValidationResult.errorMessage());
        }
    }
}
