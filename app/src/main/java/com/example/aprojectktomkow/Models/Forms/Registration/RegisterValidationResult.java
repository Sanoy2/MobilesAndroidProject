package com.example.aprojectktomkow.Models.Forms.Registration;

import com.example.aprojectktomkow.Models.Forms.IValidatorResult;

public class RegisterValidationResult implements IValidatorResult
{
    private boolean isValid;
    private String errorMessage;

    public static IValidatorResult createValidResult()
    {
        RegisterValidationResult result = new RegisterValidationResult();
        result.isValid = true;
        result.errorMessage = null;
        return result;
    }

    public static IValidatorResult createInvalidResult(String errorMessage)
    {
        RegisterValidationResult result = new RegisterValidationResult();
        result.isValid = false;
        result.errorMessage = errorMessage;
        return result;
    }

    @Override
    public boolean isValid()
    {
        return isValid;
    }

    @Override
    public String errorMessage()
    {
        return errorMessage;
    }
}
