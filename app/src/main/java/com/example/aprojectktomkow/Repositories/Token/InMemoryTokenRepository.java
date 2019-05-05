package com.example.aprojectktomkow.Repositories.Token;

import com.example.aprojectktomkow.Repositories.Token.ITokenRepository;

public class InMemoryTokenRepository implements ITokenRepository
{
    private static String token;

    @Override
    public void saveToken(String token)
    {
        if(token != null && !token.isEmpty())
        {
            this.token = token;
        }
    }

    @Override
    public void removeToken()
    {
        token = null;
    }

    @Override
    public boolean isTokenSaved()
    {
        return token != null && !token.isEmpty();
    }

    @Override
    public String getToken()
    {
        return token;
    }
}
