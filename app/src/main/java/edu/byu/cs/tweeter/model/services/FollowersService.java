package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FollowersRequest;
import edu.byu.cs.tweeter.net.response.FollowersResponse;

public class FollowersService
{

    private static FollowersService instance;
    private static final String getFollowersUri = "getfollowers";
    private final ServerFacade serverFacade;

    public static FollowersService getInstance()
    {
        if (instance == null)
        {
            instance = new FollowersService();
        }

        return instance;
    }

    private FollowersService()
    {
        serverFacade = new ServerFacade();
    }

    public FollowersResponse getFollowers(FollowersRequest followersRequest)
    {
        try
        {
            return serverFacade.getFollowers(followersRequest, getFollowersUri);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new FollowersResponse("Server error");
        }
    }
}
