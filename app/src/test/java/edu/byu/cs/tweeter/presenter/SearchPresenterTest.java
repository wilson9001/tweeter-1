package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.SearchService;
import edu.byu.cs.tweeter.net.request.SearchRequest;
import edu.byu.cs.tweeter.net.response.SearchResponse;

import static org.junit.jupiter.api.Assertions.*;

class SearchPresenterTest implements SearchPresenter.View
{
    private SearchPresenter searchPresenterSpy;
    private static SearchRequest searchRequest = new SearchRequest("");

    @BeforeEach
    void setup()
    {
        searchPresenterSpy = Mockito.spy(new SearchPresenter(this));
    }

    private SearchResponse mockSearch(SearchRequest searchRequest, SearchResponse searchResponse)
    {
        SearchService searchServiceMock = Mockito.mock(SearchService.class);
        Mockito.when(searchServiceMock.search(Mockito.any(SearchRequest.class))).thenReturn(searchResponse);
        Mockito.when(searchPresenterSpy.getService()).thenReturn(searchServiceMock);

        return searchPresenterSpy.search(searchRequest);
    }

    @Test
    void searchSuccess()
    {
        SearchResponse response = mockSearch(searchRequest, new SearchResponse(new User("", "", ""), false));

        assertNull(response.getMessage());
        assertNotNull(response.getSearchedUser());
        assertFalse(response.getUserFollowsSearchedUser());
    }

    @Test
    void searchFailure()
    {
        SearchResponse searchResponse = mockSearch(searchRequest, new SearchResponse(""));

        assertNotNull(searchResponse.getMessage());
        assertNull(searchResponse.getSearchedUser());
        assertFalse(searchResponse.getUserFollowsSearchedUser());
    }
}