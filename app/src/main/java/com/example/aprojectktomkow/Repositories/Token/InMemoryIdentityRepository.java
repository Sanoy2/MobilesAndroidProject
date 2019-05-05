package com.example.aprojectktomkow.Repositories.Token;

public class InMemoryIdentityRepository implements IIdentityRepository
{
    private static String token;
    private static String email;
    private static String username;

    @Override
    public void login(String token, String username, String email)
    {
        setEmail(email);
        setUsername(username);
        setToken(token);
    }

    @Override
    public void logout()
    {
        token = null;
        email = null;
        username = null;
    }

    @Override
    public boolean isUserLogged()
    {
        return isOk(token) && isOk(email) && isOk(username);
    }

    @Override
    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
       if(isOk(token))
       {
           this.token = token;
       }
    }

    private String getEmail()
    {
        return email;
    }

    private void setEmail(String email)
    {
        if(isOk(email))
        {
            this.email = email;
        }
    }

    private String getUsername()
    {
        return username;
    }

    private void setUsername(String username)
    {
        if(isOk(username))
        {
            this.username = username;
        }
    }

    private boolean isOk(String anything)
    {
        return anything != null && !anything.isEmpty();
    }
}
