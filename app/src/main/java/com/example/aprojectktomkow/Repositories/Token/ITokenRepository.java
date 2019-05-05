package com.example.aprojectktomkow.Repositories.Token;

public interface ITokenRepository
{
    void saveToken(String token);
    void removeToken();
    boolean isTokenSaved();
    String getToken();
}
