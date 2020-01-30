package edu.byu.cs.tweeter.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FeedRequest
{
    private final User owner;
    private final int limit;
    //TODO: Create status class and reference here

    public FeedRequest(User owner, int limit/*, Status lastStatus*/)
    {
        this.owner = owner;
        this.limit = limit;
        //TODO: set lastStatus here
    }

    public User getOwner() { return owner; }
    public int getLimit() { return limit; }
    //TODO: return lastStatus here

}
