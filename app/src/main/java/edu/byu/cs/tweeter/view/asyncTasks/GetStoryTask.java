package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;

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
        return storyPresenter.getStory(storyRequests[0]);
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
