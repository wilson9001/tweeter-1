package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.SearchRequest;
import edu.byu.cs.tweeter.net.response.SearchResponse;

public class SearchService
{
    private static SearchService instance;
    private static ServerFacade serverFacade;
    private static final String searchUrl = "/search";

    public static SearchService getInstance()
    {
        if (instance == null)
        {
            instance = new SearchService();
        }

        return instance;
    }

    private SearchService()
    {
        serverFacade = new ServerFacade();
    }

    public SearchResponse search(SearchRequest searchRequest)
    {
        try
        {
            return serverFacade.search(searchRequest, searchUrl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new SearchResponse("Server error");
        }
    }
}
