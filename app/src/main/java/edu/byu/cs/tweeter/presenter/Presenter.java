package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;

public abstract class Presenter
{
    private LoginService getParentClassService()
    {
        return LoginService.getInstance();
    }

    public User getCurrentUser()
    {
        return getParentClassService().getCurrentUser();
    }

    public void clearCurrentUser()
    {
        getParentClassService().clearCurrentUser();
    }

    public User getUserBeingViewed()
    {
        return getParentClassService().getUserBeingViewed();
    }

    public void clearUserBeingViewed()
    {
        getParentClassService().clearUserBeingViewed();
    }

    public boolean getUserFollowsUserBeingViewed()
    {
        return getParentClassService().getUserFollowsUserBeingViewed();
    }
}
