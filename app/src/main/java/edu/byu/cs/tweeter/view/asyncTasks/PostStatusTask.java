package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.presenter.PostStatusPresenter;

public class PostStatusTask extends AsyncTask<PostStatusRequest, Void, PostStatusResponse>
{
    private final PostStatusPresenter postStatusPresenter;
    private final PostStatusObserver postStatusObserver;

    public interface PostStatusObserver
    {
        void postStatusRetrieved(PostStatusResponse postStatusResponse);
    }

    public PostStatusTask(PostStatusPresenter postStatusPresenter, PostStatusObserver postStatusObserver)
    {
        this.postStatusPresenter = postStatusPresenter;
        this.postStatusObserver = postStatusObserver;
    }

    @Override
    protected PostStatusResponse doInBackground(PostStatusRequest... postStatusRequests)
    {
        return postStatusPresenter.postStatus(postStatusRequests[0]);
    }

    @Override
    protected void onPostExecute(PostStatusResponse postStatusResponse)
    {
        if (postStatusObserver != null)
        {
            postStatusObserver.postStatusRetrieved(postStatusResponse);
        }
    }
}
