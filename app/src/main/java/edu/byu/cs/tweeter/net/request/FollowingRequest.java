package edu.byu.cs.tweeter.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowingRequest {

    public User follower;
    public int limit;
    public User lastFollowee;

    public FollowingRequest(User follower, int limit, User lastFollowee)
    {
        this.follower = follower;
        this.limit = limit;
        this.lastFollowee = lastFollowee;
    }

    public User getFollower()
    {
        return follower;
    }

    public int getLimit()
    {
        return limit;
    }

    public User getLastFollowee()
    {
        return lastFollowee;
    }
}
