package com.example.aprojectktomkow.Models.Forms.Recipe;

import java.util.HashMap;

public class NewRecipeCommand
{
    private String name;
    private String shortDescription;
    private String description;
    private String neededTimeMinutes;
    private boolean isPrivate;
    private String imageUrl;

    public NewRecipeCommand(NewRecipeForm recipeForm)
    {
        name = recipeForm.getName();
        shortDescription = recipeForm.getShortDescription();
        description = recipeForm.getDescription();
        neededTimeMinutes = recipeForm.getNeededTimeMinutes();
        isPrivate = recipeForm.isPrivate();
        imageUrl = recipeForm.getImageUrl();
    }

    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("shortDescription", shortDescription);
        map.put("description", description);
        map.put("neededTimeMinutes", neededTimeMinutes);
        String isPrivateString = String.valueOf(isPrivate);
        map.put("isPrivate", isPrivateString);
        map.put("imageUrl", imageUrl);
        return map;
    }
}
