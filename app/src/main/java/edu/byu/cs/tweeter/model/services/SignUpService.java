package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.SignUpRequest;
import edu.byu.cs.tweeter.net.response.SignUpResponse;

public class SignUpService
{
    private static SignUpService instance;
    private final ServerFacade serverFacade;
    private static final String signUpUrl = "/signup";

    public static SignUpService getInstance()
    {
        if (instance== null)
        {
            instance = new SignUpService();
        }

        return instance;
    }

    private SignUpService()
    {
        serverFacade = new ServerFacade();
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest)
    {
        try
        {
            return serverFacade.signUp(signUpRequest, signUpUrl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new SignUpResponse("Server error");
        }
    }
}
