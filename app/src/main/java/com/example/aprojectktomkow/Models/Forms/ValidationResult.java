package com.example.aprojectktomkow.Models.Forms;

import java.util.List;

public class ValidationResult implements IValidatorResult
{
    private boolean isValid;
    private String errorMessage;

    public static IValidatorResult createValidResult()
    {
        ValidationResult result = new ValidationResult();
        result.isValid = true;
        result.errorMessage = null;
        return result;
    }

    public static IValidatorResult createInvalidResult(String errorMessage)
    {
        ValidationResult result = new ValidationResult();
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
            return ValidationResult.createValidResult();
        }
        return ValidationResult.createInvalidResult(errorMessageBuilder.toString());
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
