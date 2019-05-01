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
}
