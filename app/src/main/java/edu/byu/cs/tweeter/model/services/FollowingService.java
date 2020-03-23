package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.response.FollowingResponse;

public class FollowingService
{
    private static final String getFolloweesURI = "/getfollowees";
    private static FollowingService instance;
    private final ServerFacade serverFacade;

    public static FollowingService getInstance()
    {
        if (instance == null)
        {
            instance = new FollowingService();
        }

        return instance;
    }

    private FollowingService()
    {
        serverFacade = new ServerFacade();
    }

    public FollowingResponse getFollowees(FollowingRequest request)
    {
        try
        {
            return serverFacade.getFollowees(request, getFolloweesURI);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new FollowingResponse("Server error");
        }
    }
}
