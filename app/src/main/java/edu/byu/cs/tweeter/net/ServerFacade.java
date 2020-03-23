package edu.byu.cs.tweeter.net;

import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.ChangeRelationshipRequest;
import edu.byu.cs.tweeter.net.request.ChangeRelationshipRequest_Net;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.FeedRequest_Net;
import edu.byu.cs.tweeter.net.request.FollowersRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.request.PostStatusRequest_Net;
import edu.byu.cs.tweeter.net.request.SearchRequest;
import edu.byu.cs.tweeter.net.request.SearchRequest_Net;
import edu.byu.cs.tweeter.net.request.SignInRequest;
import edu.byu.cs.tweeter.net.request.SignOutRequest;
import edu.byu.cs.tweeter.net.request.SignOutRequest_Net;
import edu.byu.cs.tweeter.net.request.SignUpRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.ChangeRelationshipResponse;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.net.response.FeedResponse_Simple;
import edu.byu.cs.tweeter.net.response.FollowersResponse;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.net.response.SearchResponse;
import edu.byu.cs.tweeter.net.response.SignInResponse;
import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.net.response.SignUpResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse_Simple;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade
{

    // TODO: Set this the the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://035ackko47.execute-api.us-west-2.amazonaws.com/CS340";
    private static User currentUser, userBeingViewed;
    private static boolean userFollowsUserBeingViewed;
    private static String authToken;

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath) throws IOException
    {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
    }

    public StoryResponse getStory(StoryRequest storyRequest, String urlPath) throws IOException
    {
        return new ClientCommunicator(SERVER_URL).doPost(urlPath, storyRequest, null, StoryResponse_Simple.class).toStoryResponse();
    }

    public FollowersResponse getFollowers(FollowersRequest followersRequest, String urlPath) throws IOException
    {
        Log.d("ServerFacade", "getFollowersRequest.followee:".concat(followersRequest.followee.toString()));
        return new ClientCommunicator(SERVER_URL).doPost(urlPath, followersRequest, null, FollowersResponse.class);
    }

    public FeedResponse getFeed(FeedRequest feedRequest, String urlPath) throws IOException
    {
        return new ClientCommunicator(SERVER_URL).doPost(urlPath, new FeedRequest_Net(authToken, feedRequest), null, FeedResponse_Simple.class).toFeedResponse();
    }

    public SignInResponse signIn(SignInRequest signInRequest, String urlPath) throws IOException
    {
        SignInResponse response = new ClientCommunicator(SERVER_URL).doPost(urlPath, signInRequest, null, SignInResponse.class);

        if (response.getMessage() == null)
        {
            Log.d("signIn", "response message is null");
            if(response.getAuthToken() == null)
            {
                Log.d("signIn", "no message returned from sign in but no authtoken");
            }

            authToken = response.getAuthToken();

            if(response.getUser() == null)
            {
                Log.d("signIn", "no message returned from sign in but no user");
            }

            currentUser = response.getUser();
            userBeingViewed = currentUser;
            userFollowsUserBeingViewed = false;
        }

        return response;
    }

    public User currentUser()
    {
        if (currentUser == null)
        {
            currentUser = new User("Test", "User", "");
            authToken = "HorriblyInsecureAuthtokenForUser.@TestUser";
            userFollowsUserBeingViewed = false;
        }

        return currentUser;
    }

    public void clearCurrentUser()
    {
        currentUser = null;
        authToken = null;
        userFollowsUserBeingViewed = false;
    }

    public User userViewing()
    {
        if (userBeingViewed == null)
        {
            userBeingViewed = new User("Test", "User", "");
            userFollowsUserBeingViewed = false;
        }

        return userBeingViewed;
    }

    public void clearUserBeingViewed()
    {
        userBeingViewed = null;
        userFollowsUserBeingViewed = false;
    }

    public SignOutResponse signOut(SignOutRequest signOutRequest, String urlPath) throws IOException
    {
        SignOutResponse response = new ClientCommunicator(SERVER_URL).doPost(urlPath, new SignOutRequest_Net(authToken, signOutRequest), null, SignOutResponse.class);

        if (response.getMessage() == null)
        {
            clearCurrentUser();
            clearUserBeingViewed();
            authToken = null;
            userFollowsUserBeingViewed = false;
        }

        return response;
    }

    public boolean userFollowsUserBeingViewed()
    {
        return userFollowsUserBeingViewed;
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest, String urlPath) throws IOException
    {
        SignUpResponse response = new ClientCommunicator(SERVER_URL).doPost(urlPath, signUpRequest, null, SignUpResponse.class);

        if (response.getMessage() == null)
        {
            currentUser = response.getUser();
            authToken = response.getAuthToken();
            userBeingViewed = currentUser;
            userFollowsUserBeingViewed = false;
        }

        return response;
    }

    public SearchResponse search(SearchRequest searchRequest, String urlPath) throws IOException
    {
        SearchResponse response = new ClientCommunicator(SERVER_URL).doPost(urlPath, new SearchRequest_Net(searchRequest, authToken), null, SearchResponse.class);

        if (response.getMessage() == null)
        {
            userBeingViewed = response.getSearchedUser();
            userFollowsUserBeingViewed = response.getUserFollowsSearchedUser();
        }

        return response;
    }

    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest, String urlPath) throws IOException
    {
        return new ClientCommunicator(SERVER_URL).doPut(urlPath, new PostStatusRequest_Net(authToken, postStatusRequest), null, PostStatusResponse.class);
    }

    public ChangeRelationshipResponse changeRelationship(ChangeRelationshipRequest changeRelationshipRequest, String urlPath) throws IOException
    {
        ChangeRelationshipResponse response = new ClientCommunicator(SERVER_URL).doPost(urlPath, new ChangeRelationshipRequest_Net(authToken, changeRelationshipRequest), null, ChangeRelationshipResponse.class);

        if (response.getMessage() == null)
        {
            userFollowsUserBeingViewed = response.getRelationshipChanged() == ChangeRelationshipResponse.RelationshipChanged.FOLLOWED;
        }

        return response;
    }
}
