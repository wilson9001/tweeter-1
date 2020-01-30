package edu.byu.cs.tweeter.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class StoryRequest {
    private final User owner;
    private final int limit;
    //TODO: create story class and add as private data member here

    public StoryRequest(User owner, int limit/*, add in last status here*/)
    {
        this.owner = owner;
        this.limit = limit;
        //TODO: set last status here
    }

    public User getOwner() { return owner; }
    public int getLimit() { return limit; }
    //TODO: create getter for last status here
}
