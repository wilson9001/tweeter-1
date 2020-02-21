package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.SearchService;
import edu.byu.cs.tweeter.net.request.SearchRequest;
import edu.byu.cs.tweeter.net.response.SearchResponse;

public class SearchPresenter extends Presenter
{
    private final View view;

    public interface View
    {

    }

    public SearchPresenter(View view)
    {
        this.view = view;
    }

    public SearchService getService()
    {
        return SearchService.getInstance();
    }

    public SearchResponse search(SearchRequest searchRequest)
    {
        return getService().search(searchRequest);
    }
}
