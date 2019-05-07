package com.example.aprojectktomkow.Providers;

public class ApiUrl
{
    private static final String apiUrl = "http://ktomkow.sytes.net/api/";

    public static String getApiUrl()
    {
        return String.valueOf(apiUrl);
    }

    public static String getUsersUrl()
    {
        return apiUrl + "users";
    }

    public static String getUserRegisterUrl()
    {
        return apiUrl + "users/register";
    }

    public static String getUserLoginUlr()
    {
        return apiUrl + "users/login";
    }

    public static String getRecipesUrlNonLoggedUser()
    {
        return apiUrl + "recipes";
    }

    public static String getRecipesUrlLoggedUser()
    {
        return apiUrl + "recipes/loggeduser";
    }

    public static String getRecipesUrlCreate() { return apiUrl + "recipes"; }
}
