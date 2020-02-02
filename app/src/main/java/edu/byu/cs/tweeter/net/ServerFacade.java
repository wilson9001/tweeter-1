package edu.byu.cs.tweeter.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.FollowersRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.net.response.FollowersResponse;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.PagedResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;

public class ServerFacade
{

    private static Map<User, List<User>> followerToFollowees;
    private static Map<User, List<User>> followeeToFollowers;
    private static List<Follow> follows;

    public FollowingResponse getFollowees(FollowingRequest request)
    {

        assert request.getLimit() >= 0;
        assert request.getFollower() != null;

        if (followerToFollowees == null)
        {
            followerToFollowees = initializeFollowees();
        }

        List<User> allFollowees = followerToFollowees.get(request.getFollower());
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0)
        {
            if (allFollowees != null)
            {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowees);

                for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++)
                {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    public FollowersResponse getFollowers(FollowersRequest followersRequest)
    {

        assert followersRequest.getLimit() >= 0;
        assert followersRequest.getFollowee() != null;

        if (followeeToFollowers == null)
        {
            followeeToFollowers = initializeFollowers();
        }

        List<User> allFollowers = followeeToFollowers.get(followersRequest.getFollowee());
        List<User> responseFollowers = new ArrayList<>(followersRequest.getLimit());

        boolean hasMorePages = false;

        if (followersRequest.getLimit() > 0)
        {
            if (allFollowers != null)
            {
                int followersIndex = getFollowersStartingIndex(followersRequest.getLastFollower(), allFollowers);

                for (int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < followersRequest.getLimit(); followersIndex++, limitCounter++)
                {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
        }

        return new FollowersResponse(responseFollowers, hasMorePages);
    }

    public StoryResponse getStory(StoryRequest storyRequest)
    {

        //TODO: Fill in functionality
        return null;
    }

    public FeedResponse getFeed(FeedRequest feedRequest)
    {

        //TODO: Fill in functionality
        return null;
    }

    //TODO: implement private helper functions for followers
    private int getFollowersStartingIndex(User lastFollower, List<User> allFollowers)
    {
        int followersIndex = 0;

        if (lastFollower != null)
        {
            /*for (int i = 0; i < allFollowers.size(); i++)
            {
                if(lastFollower.equals(allFollowers.get(i)))
                {
                    followersIndex = i + 1;
                }
            }*/

            //TODO: Determine if this works or if there's something weird with equals() which makes the provided way necessary.
            return allFollowers.indexOf(lastFollower) + 1;
        }

        return followersIndex;
    }

    /**
     * Generates the followers data
     */
    private Map<User, List<User>> initializeFollowers()
    {
        Map<User, List<User>> followersOfFollowee = new HashMap<>();

        if (follows == null)
        {
            follows = getFollowGenerator().generateUsersAndFollows(100, 0, 50, FollowGenerator.Sort.FOLLOWEE_FOLLOWER);
        }

        for (Follow follow : follows)
        {
            List<User> followers = followersOfFollowee.get(follow.getFollowee());

            if (followers == null)
            {
                followers = new ArrayList<>();
                followersOfFollowee.put(follow.getFollowee(), followers);
            }

            followers.add(follow.getFollower());
        }

        return followersOfFollowee;
    }

    //TODO: implement private helper functions for story
    //TODO: implement private helper functions for feed

    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees)
    {

        int followeesIndex = 0;

        if (lastFollowee != null)
        {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            /*for (int i = 0; i < allFollowees.size(); i++)
            {
                if (lastFollowee.equals(allFollowees.get(i)))
                {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                }
            }*/

            //TODO: See if this works fine or if there's something weird with equals() which makes the provided way necessary
            followeesIndex = allFollowees.indexOf(lastFollowee) + 1;
        }

        return followeesIndex;
    }

    /**
     * Generates the followee data.
     */
    private Map<User, List<User>> initializeFollowees()
    {

        Map<User, List<User>> followeesByFollower = new HashMap<>();

        if (follows == null)
        {
            follows = getFollowGenerator().generateUsersAndFollows(100, 0, 50, FollowGenerator.Sort.FOLLOWER_FOLLOWEE);
        }

        // Populate a map of followees, keyed by follower so we can easily handle followee requests
        for (Follow follow : follows)
        {
            List<User> followees = followeesByFollower.get(follow.getFollower());

            if (followees == null)
            {
                followees = new ArrayList<>();
                followeesByFollower.put(follow.getFollower(), followees);
            }

            followees.add(follow.getFollowee());
        }

        return followeesByFollower;
    }

    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    FollowGenerator getFollowGenerator()
    {
        return FollowGenerator.getInstance();
    }

    StatusGenerator getStatusGenerator()
    {
        return StatusGenerator.getInstance();
    }
}
