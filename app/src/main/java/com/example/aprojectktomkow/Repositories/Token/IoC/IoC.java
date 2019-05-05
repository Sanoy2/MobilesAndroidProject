package com.example.aprojectktomkow.Repositories.Token.IoC;

import com.example.aprojectktomkow.Repositories.Token.IIdentityRepository;
import com.example.aprojectktomkow.Repositories.Token.InMemoryIdentityRepository;

public class IoC
{
    public static IIdentityRepository getIdentityRepository()
    {
        return new InMemoryIdentityRepository();
    }
}
