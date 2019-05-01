package com.example.aprojectktomkow.Encryption;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypter implements IEncrypter
{

    @Override
    public String sha256(String text)
    {
        MessageDigest md = null;
        try
        {
            md.update( text.getBytes( StandardCharsets.UTF_8 ) );
            byte[] digest = md.digest();
            String hex = String.format( "%064x", new BigInteger( 1, digest ) );

            return hex;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
