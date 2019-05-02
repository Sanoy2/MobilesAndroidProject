package com.example.aprojectktomkow.Models.Forms.Login;
import com.example.aprojectktomkow.Encryption.Encrypter;
import com.example.aprojectktomkow.Encryption.IEncrypter;
import java.util.HashMap;

public class LoginCommand
{
    private String email;
    private String password;

    public LoginCommand(LoginForm form)
    {
            email = form.getEmail();
            IEncrypter encrypter = new Encrypter();
            password = encrypter.sha256(form.getPassword());
    }

    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("password", password);
        return map;
    }
}
