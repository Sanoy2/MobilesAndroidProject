package com.example.aprojectktomkow.Models;

import java.util.Date;

public class Recipe
{
    private int id;
    private int authorId;
    private String name;
    private String shortDescription;
    private String description;
    private int neededTimeMinutes;
    private Date dateOfLastModification;
    private String mainImageUrl;
    private boolean isPrivate;

    public Recipe() {}

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAuthorId()
    {
        return authorId;
    }

    public void setAuthorId(int authorId)
    {
        this.authorId = authorId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getNeededTimeMinutes()
    {
        return neededTimeMinutes;
    }

    public void setNeededTimeMinutes(int neededTimeMinutes)
    {
        this.neededTimeMinutes = neededTimeMinutes;
    }

    public Date getDateOfLastModification()
    {
        return dateOfLastModification;
    }

    public void setDateOfLastModification(Date dateOfLastModification)
    {
        this.dateOfLastModification = dateOfLastModification;
    }

    public String getMainImageUrl()
    {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl)
    {
        this.mainImageUrl = mainImageUrl;
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
