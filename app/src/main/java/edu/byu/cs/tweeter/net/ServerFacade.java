package edu.byu.cs.tweeter.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.FollowersRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.net.response.FollowersResponse;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;

public class ServerFacade
{

    private static Map<User, List<User>> followerToFollowees;
    private static Map<User, List<User>> followeeToFollowers;
    private static List<Follow> follows;
    private static List<Status> statuses;
    private static Map<User, List<Status>> userToStory;
    private static Map<User, List<Status>> userToFeed;

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
        assert storyRequest.getLimit() >= 0;
        assert storyRequest.getOwner() != null;

        if (userToStory == null)
        {
            userToStory = initializeStories();
        }

        List<Status> statuses = userToStory.get(storyRequest.getOwner());

        List<Status> responseStatuses = new ArrayList<>(storyRequest.getLimit());

        boolean hasMorePages = false;

        if (statuses != null)
        {
            Collections.sort(statuses);

            int storyIndex = getStoryStartingIndex(storyRequest.getLastStatus(), statuses);

            for (int limitCounter = 0; storyIndex < statuses.size() && limitCounter < storyRequest.getLimit(); storyIndex++, limitCounter++)
            {
                responseStatuses.add(statuses.get(storyIndex));
            }

            hasMorePages = storyIndex < statuses.size();
        }

        return new StoryResponse(responseStatuses, hasMorePages);
    }

    public FeedResponse getFeed(FeedRequest feedRequest)
    {

        //TODO: Fill in functionality
        assert feedRequest.getLimit() >= 0;
        assert feedRequest.getOwner() != null;

        if (userToFeed == null)
        {
            userToFeed = initializeFeed();
        }

        List<Status> statuses = userToFeed.get(feedRequest.getOwner());

        List<Status> responseStatuses = new ArrayList<>(feedRequest.getLimit());

        boolean hasMorePages = false;

        if (statuses != null)
        {
            Collections.sort(statuses);

            int feedIndex = getFeedStartingIndex(feedRequest.getLastStatus(), statuses);

            for (int limitCounter = 0; feedIndex < statuses.size() && limitCounter < feedRequest.getLimit(); feedIndex++, limitCounter++)
            {
                responseStatuses.add(statuses.get(feedIndex));
            }

            hasMorePages = feedIndex < statuses.size();
        }

        return new FeedResponse(responseStatuses, hasMorePages);
    }

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

    private int getStoryStartingIndex(Status lastStatus, List<Status> allStatuses)
    {
        int storyIndex = 0;

        if (lastStatus != null)
        {
            return allStatuses.indexOf(lastStatus) + 1;
        }

        return storyIndex;
    }

    /*
    * Generates the status data
    * */
    private Map<User, List<Status>> initializeStories()
    {
        Map<User, List<Status>> userToOwnStatuses = new HashMap<>();

        if (followerToFollowees == null)
        {
            followerToFollowees = initializeFollowees();
        }

        if (statuses == null)
        {
            statuses = getStatusGenerator().generateStatuses(0, 50, followerToFollowees.keySet());
        }

        for (Status status : statuses)
        {
            List<Status> posterStatuses = userToOwnStatuses.get(status.getPoster());

            if (posterStatuses == null)
            {
                posterStatuses = new ArrayList<>();
                userToOwnStatuses.put(status.getPoster(), posterStatuses);
            }

            posterStatuses.add(status);
        }

        return userToOwnStatuses;
    }

    private int getFeedStartingIndex(Status lastStatus, List<Status> allStatuses)
    {
        int feedIndex = 0;

        if (lastStatus != null)
        {
            feedIndex = allStatuses.indexOf(lastStatus) + 1;
        }

        return feedIndex;
    }

    private Map<User, List<Status>> initializeFeed()
    {
        Map<User, List<Status>> userToOtherStatuses = new HashMap<>();

        if(userToStory == null)
        {
           userToStory = initializeStories();
        }

        for (User follower : followerToFollowees.keySet())
        {
            List<Status> feedStatuses = userToOtherStatuses.get(follower);

            if (feedStatuses == null)
            {
                feedStatuses = new ArrayList<>();
                userToOtherStatuses.put(follower, feedStatuses);
            }

            List<User> followees = followerToFollowees.get(follower);

            if (followees != null)
            {
                for (User followee : followees)
                {
                    List<Status> statusesFromFollowee = userToStory.get(followee);

                    if(statusesFromFollowee != null)
                    {
                        feedStatuses.addAll(statusesFromFollowee);
                    }
                }
            }
        }

        return userToOtherStatuses;
    }

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
