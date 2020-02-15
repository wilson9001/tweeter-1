package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class GetStoryTask extends AsyncTask<StoryRequest, Void, StoryResponse>
{
    private final StoryPresenter storyPresenter;
    private final GetStoryObserver getStoryObserver;

    public interface GetStoryObserver
    {
        void StoryRetrieved(StoryResponse storyResponse);
    }

    public GetStoryTask(StoryPresenter storyPresenter, GetStoryObserver getStoryObserver)
    {
        this.storyPresenter = storyPresenter;
        this.getStoryObserver = getStoryObserver;
    }

    @Override
    protected StoryResponse doInBackground(StoryRequest... storyRequests)
    {
        StoryResponse storyResponse = storyPresenter.getStory(storyRequests[0]);
        loadImages(storyResponse);
        return storyResponse;
    }

    private void loadImages(StoryResponse storyResponse)
    {
        for (edu.byu.cs.tweeter.model.domain.Status status : storyResponse.getStatuses())
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
    protected void onPostExecute(StoryResponse storyResponse)
    {
        if (getStoryObserver != null)
        {
            getStoryObserver.StoryRetrieved(storyResponse);
        }
    }
}
