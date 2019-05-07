package com.example.aprojectktomkow.Repositories.Token;

public interface IIdentityRepository
{
    void login(String token, String username, String email);
    void logout();
    boolean isUserLogged();
    String getToken();
    String getUsername();
    String getEmail();
}
