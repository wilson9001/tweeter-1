package edu.byu.cs.tweeter.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class SearchResponse
{
    private final User searchedUser;
    private final String message;

    public SearchResponse(User searchedUser)
    {
        this.searchedUser = searchedUser;
        this.message = null;
    }

    public SearchResponse(String message)
    {
        this.searchedUser = null;
        this.message = message;
    }

    public User getSearchedUser()
    {
        return searchedUser;
    }

    public String getMessage()
    {
        return message;
    }
}
