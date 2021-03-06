package edu.byu.cs.tweeter.net.response;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.SignInRequest;
import edu.byu.cs.tweeter.net.request.SignUpRequest;

public class SignUpResponse
{
    private final User signedUpUser;
    private final String authToken;
    private final String message;

    public SignUpResponse(User newUser, String authToken)
    {
        signedUpUser = newUser;
        this.authToken = authToken;
        message = null;
    }

    public SignUpResponse(String message)
    {
        this.message = message;
        signedUpUser = null;
        authToken = null;
    }

    public User getUser()
    {
        return signedUpUser;
    }

    public String getMessage()
    {
        return message;
    }

    public String getAuthToken()
    {
        return authToken;
    }
}
