package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.presenter.FeedPresenter;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.util.ImageUtils;

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
        FeedResponse feedResponse = feedPresenter.getFeed(feedRequests[0]);
        loadImages(feedResponse);
        return feedResponse;
    }

    private void loadImages(FeedResponse feedResponse)
    {
        for (edu.byu.cs.tweeter.model.domain.Status status : feedResponse.getStatuses())
        {
            User poster = status.getPoster();

            Drawable drawable;

            try
            {
                drawable = ImageUtils.drawableFromUrl(poster.getImageUrl());
            }
            catch (IOException exception)
            {
                Log.e(this.getClass().getName(), exception.toString(), exception);
                drawable = null;
            }

            ImageCache.getInstance().cacheImage(poster, drawable);
        }
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
