package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.StoryService;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.StoryResponse;

import static org.junit.jupiter.api.Assertions.*;

class StoryPresenterTest implements StoryPresenter.View
{
    private StoryPresenter storyPresenterSpy;
    private StoryRequest storyRequest = new StoryRequest(new User("", "", ""), 10 , new Status("", new User("", "", "")));

    @BeforeEach
    void setup()
    {
        storyPresenterSpy = Mockito.spy(new StoryPresenter(this));
    }

    private StoryResponse mockGetStory(StoryRequest storyRequest, StoryResponse storyResponse)
    {
        StoryService storyServiceMock = Mockito.mock(StoryService.class);
        Mockito.when(storyServiceMock.getStory(Mockito.any(StoryRequest.class))).thenReturn(storyResponse);
        Mockito.when(storyPresenterSpy.getService()).thenReturn(storyServiceMock);

        return storyPresenterSpy.getStory(storyRequest);
    }

    @Test
    void getStorySuccess()
    {
        StoryResponse storyResponse = mockGetStory(storyRequest, new StoryResponse(new ArrayList<Status>(), false));

        assertNull(storyResponse.getMessage());
        assertNotNull(storyResponse.getStatuses());
    }

    @Test
    void getStoryFailure()
    {
        StoryResponse storyResponse = mockGetStory(storyRequest, new StoryResponse(""));

        assertNotNull(storyResponse.getMessage());
        assertNull(storyResponse.getStatuses());
    }
}