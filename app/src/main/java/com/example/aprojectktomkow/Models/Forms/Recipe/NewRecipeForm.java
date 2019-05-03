package com.example.aprojectktomkow.Models.Forms.Recipe;

import com.example.aprojectktomkow.Models.Forms.IValidatorResult;
import com.example.aprojectktomkow.Models.Forms.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class NewRecipeForm
{
    private String name;
    private String shortDescription;
    private String description;
    private String neededTimeMinutes;
    private boolean isPrivate;
    private String imageUrl;

    public IValidatorResult Validate()
    {
        List<IValidatorResult> validationResults = new ArrayList<>();
        validationResults.add(validateName());
        validationResults.add(validateTime());

        return ValidationResult.createResult(validationResults);
    }

    private IValidatorResult validateName()
    {
        if(name != null && !name.isEmpty())
        {
            return ValidationResult.createValidResult();
        }
        return ValidationResult.createInvalidResult("Insert recipe name\n");
    }

    private IValidatorResult validateTime()
    {
        if(neededTimeMinutes != null && !neededTimeMinutes.isEmpty())
        {
            return ValidationResult.createValidResult();
        }
        return ValidationResult.createInvalidResult("Insert recipe needed time\n");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String mame)
    {
        this.name = mame.trim();
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription.trim();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description.trim();
    }

    public String getNeededTimeMinutes()
    {
        return neededTimeMinutes;
    }

    public void setNeededTimeMinutes(String neededTimeMinutes)
    {
        this.neededTimeMinutes = neededTimeMinutes.trim();
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public boolean isPrivate()
    {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate)
    {
        isPrivate = aPrivate;
    }
}
