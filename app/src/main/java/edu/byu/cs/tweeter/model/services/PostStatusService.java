package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.response.PostStatusResponse;

public class PostStatusService
{
    private static PostStatusService instance;
    private static final String postStatusUrl = "poststatus";
    private static ServerFacade serverFacade;

    public static PostStatusService getInstance()
    {
        if (instance == null)
        {
            instance = new PostStatusService();
        }

        return instance;
    }

    private PostStatusService()
    {
        serverFacade = new ServerFacade();
    }

    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest)
    {
        try
        {
            return serverFacade.postStatus(postStatusRequest, postStatusUrl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new PostStatusResponse("Server error");
        }
    }
}
