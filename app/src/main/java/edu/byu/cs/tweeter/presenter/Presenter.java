package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;

public abstract class Presenter
{

    public User getCurrentUser()
    {
        return LoginService.getInstance().getCurrentUser();
    }

    public void clearCurrentUser()
    {
        LoginService.getInstance().clearCurrentUser();
    }

    public User getUserBeingViewed()
    {
        return LoginService.getInstance().getUserBeingViewed();
    }

    public void clearUserBeingViewed()
    {
        LoginService.getInstance().clearUserBeingViewed();
    }
}
