package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.SearchRequest;
import edu.byu.cs.tweeter.net.response.SearchResponse;
import edu.byu.cs.tweeter.presenter.SearchPresenter;

public class SearchTask extends AsyncTask<SearchRequest, Void, SearchResponse>
{
    private final SearchPresenter searchPresenter;
    private final SearchObserver searchObserver;

    public interface SearchObserver
    {
        void searchRetrieved(SearchResponse searchResponse);
    }

    public SearchTask(SearchPresenter searchPresenter, SearchObserver searchObserver)
    {
        this.searchPresenter = searchPresenter;
        this.searchObserver = searchObserver;
    }

    @Override
    protected SearchResponse doInBackground(SearchRequest... searchRequests)
    {
        return searchPresenter.search(searchRequests[0]);
    }

    @Override
    protected void onPostExecute(SearchResponse searchResponse)
    {
        if (searchObserver != null)
        {
            searchObserver.searchRetrieved(searchResponse);
        }
    }
}
