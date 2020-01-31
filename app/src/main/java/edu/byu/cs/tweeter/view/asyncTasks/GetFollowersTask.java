package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.FollowersRequest;
import edu.byu.cs.tweeter.net.response.FollowersResponse;
import edu.byu.cs.tweeter.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class GetFollowersTask extends AsyncTask<FollowersRequest, Void, FollowersResponse>
{

    private final FollowersPresenter followersPresenter;
    private final GetFollowersObserver getFollowersObserver;

    public interface GetFollowersObserver
    {
        void followersRetrieved(FollowersResponse followersResponse);
    }

    public GetFollowersTask(FollowersPresenter followersPresenter, GetFollowersObserver getFollowersObserver)
    {
        this.followersPresenter = followersPresenter;
        this.getFollowersObserver = getFollowersObserver;
    }

    @Override
    protected FollowersResponse doInBackground(FollowersRequest... followersRequests)
    {
        FollowersResponse followersResponse = followersPresenter.getFollowers(followersRequests[0]);
        loadImages(followersResponse);
        return followersResponse;
    }

    private void loadImages(FollowersResponse followersResponse)
    {
        for (User user : followersResponse.getFollowers())
        {
            Drawable drawable;

            try
            {
                drawable = ImageUtils.drawableFromUrl(user.getImageUrl());
            }
            catch (IOException ioException)
            {
                Log.e(this.getClass().getName(), ioException.toString(), ioException);
                drawable = null;
            }

            ImageCache.getInstance().cacheImage(user, drawable);
        }
    }

    @Override
    protected void onPostExecute(FollowersResponse followersResponse)
    {
        if (getFollowersObserver != null)
        {
            getFollowersObserver.followersRetrieved(followersResponse);
        }
    }
}
