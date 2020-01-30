package edu.byu.cs.tweeter.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowersResponse extends PagedResponse {

    private List<User> followers;

    public FollowersResponse(String message)
    {
        super(false, message, false);
    }

    public FollowersResponse(List<User> followers, boolean hasMorePages)
    {
        super(true, hasMorePages);
        this.followers = followers;
    }

    public List<User> getFollowers()
    {
        return followers;
    }
}
