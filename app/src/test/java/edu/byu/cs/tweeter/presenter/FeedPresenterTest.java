package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.FeedService;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;

import static org.junit.jupiter.api.Assertions.*;

class FeedPresenterTest implements FeedPresenter.View
{
    private FeedPresenter feedPresenterSpy;
    private static FeedRequest feedRequest = new FeedRequest(new User("User", "a", ""), 10, new Status("This is a status", new User("User", "B", "")));

    @BeforeEach
    void setup()
    {
        feedPresenterSpy = Mockito.spy(new FeedPresenter(this));
    }

    @Test
    void getFeedSuccess()
    {
        FeedResponse feedResponse = mockGetFeed(feedRequest, new FeedResponse(new ArrayList<Status>(), false));

        assertNull(feedResponse.getMessage());
        assertNotNull(feedResponse.getStatuses());
        assertFalse(feedResponse.hasMorePages());
    }

    @Test
    void getFeedError()
    {
        FeedResponse feedResponse = mockGetFeed(feedRequest, new FeedResponse("User not found"));

        assertNotNull(feedResponse.getMessage());
        assertNull(feedResponse.getStatuses());
        assertFalse(feedResponse.hasMorePages());
    }

    private FeedResponse mockGetFeed(FeedRequest feedRequest, FeedResponse feedResponse)
    {
        FeedService mockFeedService = Mockito.mock(FeedService.class);

        Mockito.when(mockFeedService.getFeed(Mockito.any(FeedRequest.class))).thenReturn(feedResponse);

        Mockito.when(feedPresenterSpy.getService()).thenReturn(mockFeedService);

        return feedPresenterSpy.getFeed(feedRequest);
    }
}