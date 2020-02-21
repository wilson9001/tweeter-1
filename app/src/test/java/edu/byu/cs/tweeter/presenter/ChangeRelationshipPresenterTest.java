package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.ChangeRelationshipService;
import edu.byu.cs.tweeter.net.request.ChangeRelationshipRequest;
import edu.byu.cs.tweeter.net.response.ChangeRelationshipResponse;

import static org.junit.jupiter.api.Assertions.*;

class ChangeRelationshipPresenterTest implements ChangeRelationshipPresenter.View
{
    private ChangeRelationshipPresenter changeRelationshipPresenterSpy;

    private static ChangeRelationshipRequest followRequest = new ChangeRelationshipRequest(new User("User", "A", ""), new User("User", "B", ""), ChangeRelationshipRequest.RelationshipChange.FOLLOW);
    private static ChangeRelationshipRequest unFollowRequest = new ChangeRelationshipRequest(new User("User", "A", ""), new User("User", "B", ""), ChangeRelationshipRequest.RelationshipChange.UNFOLLOW);

    @BeforeEach
    void setUp()
    {
        changeRelationshipPresenterSpy = Mockito.spy(new ChangeRelationshipPresenter(this));
    }

    @Test
    void followError()
    {
        ChangeRelationshipResponse response = changeRelationshipMock(followRequest, new ChangeRelationshipResponse("User not found"));

        assertNotNull(response.getMessage());
    }

    @Test
    void unfollowError()
    {
        ChangeRelationshipResponse response = changeRelationshipMock(unFollowRequest, new ChangeRelationshipResponse("User not found"));

        assertNotNull(response.getMessage());
    }

    @Test
    void followSuccess()
    {
        ChangeRelationshipResponse response = changeRelationshipMock(followRequest, new ChangeRelationshipResponse(ChangeRelationshipResponse.RelationshipChanged.FOLLOWED));

        assertNull(response.getMessage());
        assertEquals(ChangeRelationshipResponse.RelationshipChanged.FOLLOWED, response.getRelationshipChanged());
    }

    @Test
    void unfollowSuccess()
    {
        ChangeRelationshipResponse changeRelationshipResponse = changeRelationshipMock(unFollowRequest, new ChangeRelationshipResponse(ChangeRelationshipResponse.RelationshipChanged.UNFOLLOWED));

        assertNull(changeRelationshipResponse.getMessage());
        assertEquals(ChangeRelationshipResponse.RelationshipChanged.UNFOLLOWED, changeRelationshipResponse.getRelationshipChanged());
    }

    private ChangeRelationshipResponse changeRelationshipMock (ChangeRelationshipRequest changeRelationshipRequest, ChangeRelationshipResponse changeRelationshipResponse)
    {
        ChangeRelationshipService mockChangeRelationshipService = Mockito.mock(ChangeRelationshipService.class);
        //Mockito.when(mockChangeRelationshipService.changeRelationship(Mockito.any(ChangeRelationshipRequest.class))).thenReturn(changeRelationshipResponse);
        Mockito.when(mockChangeRelationshipService.changeRelationship(Mockito.any(ChangeRelationshipRequest.class))).thenReturn(changeRelationshipResponse);

        Mockito.when(changeRelationshipPresenterSpy.getService()).thenReturn(mockChangeRelationshipService);

        return changeRelationshipPresenterSpy.changeRelationship(changeRelationshipRequest);
    }
}