package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.PostStatusService;
import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.response.PostStatusResponse;

import static org.junit.jupiter.api.Assertions.*;

class PostStatusPresenterTest implements PostStatusPresenter.View
{
    private PostStatusPresenter postStatusPresenterSpy;
    private static PostStatusRequest postStatusRequest = new PostStatusRequest(new User("", "", ""), "");

    @BeforeEach
    void setup()
    {
        postStatusPresenterSpy = Mockito.spy(new PostStatusPresenter(this));
    }

    @Test
    void postSuccess()
    {
        PostStatusResponse postStatusResponse = mockPost(postStatusRequest, new PostStatusResponse());

        assertNull(postStatusResponse.getMessage());
    }

    @Test
    void postFail()
    {
        PostStatusResponse postStatusResponse = mockPost(postStatusRequest, new PostStatusResponse("fail"));

        assertNotNull(postStatusResponse.getMessage());
    }

    private PostStatusResponse mockPost(PostStatusRequest postStatusRequest, PostStatusResponse postStatusResponse)
    {
        PostStatusService mockService = Mockito.mock(PostStatusService.class);
        Mockito.when(mockService.postStatus(Mockito.any(PostStatusRequest.class))).thenReturn(postStatusResponse);
        Mockito.when(postStatusPresenterSpy.getService()).thenReturn(mockService);

        return postStatusPresenterSpy.postStatus(postStatusRequest);
    }
}