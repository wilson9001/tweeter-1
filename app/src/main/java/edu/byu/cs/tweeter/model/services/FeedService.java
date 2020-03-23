package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;

public class FeedService {

    private static FeedService instance;
    private static final String getFeedUri = "getfeed";
    private final ServerFacade serverFacade;

    public static FeedService getInstance()
    {
        if (instance == null)
        {
            instance = new FeedService();
        }

        return instance;
    }

    private FeedService() {this.serverFacade = new ServerFacade();}

    public FeedResponse getFeed(FeedRequest feedRequest)
    {
        try
        {
            return serverFacade.getFeed(feedRequest, getFeedUri);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new FeedResponse("Server error");
        }
    }
}
