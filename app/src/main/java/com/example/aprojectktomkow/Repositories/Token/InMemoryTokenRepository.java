package com.example.aprojectktomkow.Repositories.Token;

import com.example.aprojectktomkow.Repositories.Token.ITokenRepository;

public class InMemoryTokenRepository implements ITokenRepository
{
    private static String token;

    @Override
    public void saveToken(String token)
    {
        this.token = token;
    }

    @Override
    public String getToken()
    {
        return token;
    }
}
