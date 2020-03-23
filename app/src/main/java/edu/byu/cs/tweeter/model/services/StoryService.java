package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.StoryResponse;

public class StoryService {

    private static StoryService instance;
    private static final String getStoryUrl = "/getstory";
    private final ServerFacade serverFacade;

    public static StoryService getInstance()
    {
        if (instance == null)
        {
            instance = new StoryService();
        }

        return instance;
    }

    private StoryService() {this.serverFacade = new ServerFacade();}

    public StoryResponse getStory(StoryRequest storyRequest)
    {
        try
        {
            return serverFacade.getStory(storyRequest, getStoryUrl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new StoryResponse("Server error");
        }
    }
}
