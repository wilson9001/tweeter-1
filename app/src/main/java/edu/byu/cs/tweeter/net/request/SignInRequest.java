package edu.byu.cs.tweeter.net.request;

public class SignInRequest
{
    public String userAlias, password;

    public SignInRequest(String userAlias, String password)
    {
        this.userAlias = userAlias;
        this.password = password;
    }

    public String getUserAlias()
    {
        return userAlias;
    }

    public String getPassword()
    {
        return password;
    }
}
