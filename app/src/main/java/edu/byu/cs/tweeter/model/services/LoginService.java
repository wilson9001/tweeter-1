package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.SignInRequest;
import edu.byu.cs.tweeter.net.request.SignOutRequest;
import edu.byu.cs.tweeter.net.response.SignInResponse;
import edu.byu.cs.tweeter.net.response.SignOutResponse;

public class LoginService {

    private static LoginService instance;
    private static final String signInUrl = "/login";
    private static final String signOutUrl= "/logout";
    private final ServerFacade serverFacade;

    public static LoginService getInstance() {
        if(instance == null) {
            instance = new LoginService();
        }

        return instance;
    }

    private LoginService() {
        serverFacade = new ServerFacade();
    }

    public SignInResponse signIn(SignInRequest signInRequest)
    {
        try
        {
            return serverFacade.signIn(signInRequest, signInUrl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new SignInResponse("Server error");
        }
    }

    public User getCurrentUser() {
        return serverFacade.currentUser();
    }

    public void clearCurrentUser()
    {
        serverFacade.clearCurrentUser();
    }

    public User getUserBeingViewed()
    {
        return serverFacade.userViewing();
    }

    public void clearUserBeingViewed()
    {
        serverFacade.clearUserBeingViewed();
    }

    public SignOutResponse signOut(SignOutRequest signOutRequest)
    {
        try
        {
            return serverFacade.signOut(signOutRequest, signOutUrl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new SignOutResponse("Server error");
        }
    }

    public boolean getUserFollowsUserBeingViewed()
    {
        return serverFacade.userFollowsUserBeingViewed();
    }
}
