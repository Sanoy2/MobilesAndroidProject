package com.example.aprojectktomkow.Models.Forms.Registration;

import com.example.aprojectktomkow.Models.Forms.IValidatorResult;

import java.util.List;

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

    public static IValidatorResult createResult(List<IValidatorResult> resultList)
    {
        StringBuilder errorMessageBuilder = new StringBuilder();
        boolean isValid = true;
        for (IValidatorResult result: resultList)
        {
            if(!result.isValid())
            {
                errorMessageBuilder.append(result.errorMessage());
                isValid = false;
            }
        }

        if(isValid)
        {
            return RegisterValidationResult.createValidResult();
        }
        return RegisterValidationResult.createInvalidResult(errorMessageBuilder.toString());
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
