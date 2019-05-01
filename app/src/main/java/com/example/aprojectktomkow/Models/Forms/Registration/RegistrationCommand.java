package com.example.aprojectktomkow.Models.Forms.Registration;

import com.example.aprojectktomkow.Encryption.Encrypter;
import com.example.aprojectktomkow.Encryption.IEncrypter;
import com.example.aprojectktomkow.Models.Forms.IValidatorResult;

public class RegistrationCommand
{
    String username;
    String email;
    String password;

    public RegistrationCommand(RegistrationForm form) throws Exception
    {
        IValidatorResult formValidationResult = form.Validate();
        if(formValidationResult.isValid())
        {
            username = form.getUsername();
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
