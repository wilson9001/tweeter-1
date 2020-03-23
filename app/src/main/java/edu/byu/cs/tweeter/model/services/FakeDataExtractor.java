package edu.byu.cs.tweeter.model.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.LocalServerFacade;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.request.SearchRequest;

public class FakeDataExtractor
{
    public static class FollowerToFollowees
    {
        public Map<User, List<User>> followerToFollowees;
    }

    public static class FolloweeToFollowers
    {
        public Map<User, List<User>> followeeToFollowers;
    }

    public static class UserToStory
    {
        public Map<User, List<Status>> userToStory;
    }

    public static class UserToFeed
    {
        public Map<User, List<Status>> userToFeed;
    }

    public static class AliasesToPasswords
    {
        public Map<String, String> aliasesToPasswords;
    }

    public static class AliasesToUsers
    {
        public Map<String, User> aliasesToUsers;
    }

    public static void main(String[] args)
    {
        User TestUser = new User("Test", "User", "");

        FeedRequest feedRequest = new FeedRequest(TestUser, 0, null);
        LocalServerFacade localServerFacade = new LocalServerFacade();

        localServerFacade.getFeed(feedRequest);

        SearchRequest searchRequest = new SearchRequest("e");

        localServerFacade.search(searchRequest);

        FollowerToFollowees followerToFollowees = new FollowerToFollowees();
        FolloweeToFollowers followeeToFollowers = new FolloweeToFollowers();
        UserToStory userToStory = new UserToStory();
        UserToFeed userToFeed = new UserToFeed();
        AliasesToPasswords aliasesToPasswords = new AliasesToPasswords();
        AliasesToUsers aliasesToUsers = new AliasesToUsers();

        followerToFollowees.followerToFollowees = localServerFacade.followerToFollowees();
        followeeToFollowers.followeeToFollowers = localServerFacade.followeeToFollowers();
        userToStory.userToStory = localServerFacade.userToStory();
        userToFeed.userToFeed = localServerFacade.userToFeed();
        aliasesToPasswords.aliasesToPasswords = localServerFacade.aliasesToPasswords();
        aliasesToUsers.aliasesToUsers = localServerFacade.aliasesToUsers();

        PostStatusRequest postStatusRequest = new PostStatusRequest(TestUser, "This has @TestUser a reference @FooBar");

        localServerFacade.postStatus(postStatusRequest);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String followerToFolloweesJSON = gson.toJson(followerToFollowees);
        String followeeToFollowersJSON = gson.toJson(followeeToFollowers);
        String userToStoryJSON = gson.toJson(userToStory);
        String userToFeedJSON = gson.toJson(userToFeed);
        String aliasesToPasswordsJSON = gson.toJson(aliasesToPasswords);
        String aliasesToUsersJSON = gson.toJson(aliasesToUsers);

        try(FileWriter fileWriter = new FileWriter("followerToFollowee.json"))
        {
            fileWriter.write(followerToFolloweesJSON);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        try(FileWriter fileWriter = new FileWriter("followeeToFollowers.json"))
        {
            fileWriter.write(followeeToFollowersJSON);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        try(FileWriter fileWriter = new FileWriter("userToStory.json"))
        {
            fileWriter.write(userToStoryJSON);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        try(FileWriter fileWriter = new FileWriter("userToFeed.json"))
        {
            fileWriter.write(userToFeedJSON);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        try(FileWriter fileWriter = new FileWriter("aliasesToPasswords.json"))
        {
            fileWriter.write(aliasesToPasswordsJSON);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        try(FileWriter fileWriter = new FileWriter("aliasesToUsers.json"))
        {
            fileWriter.write(aliasesToUsersJSON);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
