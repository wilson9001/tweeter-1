package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.presenter.FeedPresenter;

public class GetFeedTask extends AsyncTask<FeedRequest, Void, FeedResponse>
{
    private final FeedPresenter feedPresenter;
    private final GetFeedObserver getFeedObserver;

    public interface GetFeedObserver
    {
        void feedRetrieved(FeedResponse feedResponse);
    }

    public GetFeedTask(FeedPresenter feedPresenter, GetFeedObserver getFeedObserver)
    {
        this.feedPresenter = feedPresenter;
        this.getFeedObserver = getFeedObserver;
    }

    @Override
    protected FeedResponse doInBackground(FeedRequest... feedRequests)
    {
        return feedPresenter.getFeed(feedRequests[0]);
    }

    @Override
    protected void onPostExecute(FeedResponse feedResponse)
    {
        if (getFeedObserver != null)
        {
            getFeedObserver.feedRetrieved(feedResponse);
        }
    }
}
