package edu.byu.cs.tweeter.net.request;

public class SearchRequest
{
    public String searchQuery;

    public SearchRequest(String searchQuery)
    {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery()
    {
        return searchQuery;
    }
}
