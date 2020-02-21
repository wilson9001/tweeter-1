package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FollowingService;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.response.FollowingResponse;

import static org.junit.jupiter.api.Assertions.*;

class FollowingPresenterTest implements FollowingPresenter.View
{
    private FollowingPresenter followingPresenterSpy;
    private static FollowingRequest followingRequest = new FollowingRequest(new User("User", "a", ""), 10, new User("User", "b", ""));

    @BeforeEach
    void setup()
    {
        followingPresenterSpy = Mockito.spy(new FollowingPresenter(this));
    }

    @Test
    void getFollowingSuccess()
    {
        FollowingResponse followingResponse = mockGetFollowing(followingRequest, new FollowingResponse(new ArrayList<User>(), false));

        assertNull(followingResponse.getMessage());
        assertNotNull(followingResponse.getFollowees());
        assertFalse(followingResponse.hasMorePages());
    }

    @Test
    void getFollowingFailure()
    {
        FollowingResponse followingResponse = mockGetFollowing(followingRequest, new FollowingResponse("User not found"));

        assertNotNull(followingResponse.getMessage());
        assertNull(followingResponse.getFollowees());
        assertFalse(followingResponse.hasMorePages());
    }

    private FollowingResponse mockGetFollowing(FollowingRequest followingRequest, FollowingResponse followingResponse)
    {
        FollowingService mockFollowingService = Mockito.mock(FollowingService.class);
        Mockito.when(mockFollowingService.getFollowees(Mockito.any(FollowingRequest.class))).thenReturn(followingResponse);
        Mockito.when(followingPresenterSpy.getService()).thenReturn(mockFollowingService);

        return followingPresenterSpy.getFollowing(followingRequest);
    }
}