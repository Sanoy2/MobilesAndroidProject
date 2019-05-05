package com.example.aprojectktomkow.Repositories.Token.IoC;

import com.example.aprojectktomkow.Repositories.Token.ITokenRepository;
import com.example.aprojectktomkow.Repositories.Token.InMemoryTokenRepository;

public class IoC
{
    public static ITokenRepository getTokenRepository()
    {
        return new InMemoryTokenRepository();
    }
}
