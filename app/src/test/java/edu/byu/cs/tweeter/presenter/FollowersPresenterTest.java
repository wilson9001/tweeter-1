package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FollowersService;
import edu.byu.cs.tweeter.net.request.FollowersRequest;
import edu.byu.cs.tweeter.net.response.FollowersResponse;

import static org.junit.jupiter.api.Assertions.*;

class FollowersPresenterTest implements FollowersPresenter.View
{
    private FollowersPresenter followersPresenterSpy;
    private static FollowersRequest followersRequest = new FollowersRequest(new User("User", "a", ""), 10, new User("User", "b", ""));

    @BeforeEach
    void setup()
    {
        followersPresenterSpy = Mockito.spy(new FollowersPresenter(this));
    }

    @Test
    void getFollowersSuccess()
    {
        FollowersResponse followersResponse = mockGetFollowers(followersRequest, new FollowersResponse(new ArrayList<User>(), false));

        assertNotNull(followersResponse.getFollowers());
        assertNull(followersResponse.getMessage());
        assertFalse(followersResponse.hasMorePages());
    }

    @Test
    void getFollowersFailure()
    {
        FollowersResponse followersResponse = mockGetFollowers(followersRequest, new FollowersResponse("User not found"));

        assertNotNull(followersResponse.getMessage());
        assertNull(followersResponse.getFollowers());
        assertFalse(followersResponse.hasMorePages());
    }

    private FollowersResponse mockGetFollowers(FollowersRequest followersRequest, FollowersResponse followersResponse)
    {
        FollowersService mockFollowersService = Mockito.mock(FollowersService.class);
        Mockito.when(mockFollowersService.getFollowers(Mockito.any(FollowersRequest.class))).thenReturn(followersResponse);
        Mockito.when(followersPresenterSpy.getService()).thenReturn(mockFollowersService);

        return followersPresenterSpy.getFollowers(followersRequest);
    }
}