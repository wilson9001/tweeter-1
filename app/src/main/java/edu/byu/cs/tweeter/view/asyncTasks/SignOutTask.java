package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.SignOutRequest;
import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class SignOutTask extends AsyncTask<SignOutRequest, Void, SignOutResponse>
{

    private final MainPresenter presenter;
    private final SignOutObserver signOutObserver;

    public interface SignOutObserver
    {
        void signOutRetrieved(SignOutResponse signOutResponse);
    }

    public SignOutTask(MainPresenter mainPresenter, SignOutObserver signOutObserver)
    {
        this.presenter = mainPresenter;
        this.signOutObserver = signOutObserver;
    }

    @Override
    protected SignOutResponse doInBackground(SignOutRequest... signOutRequests)
    {
        return presenter.signOut(signOutRequests[0]);
    }

    @Override
    protected void onPostExecute(SignOutResponse signOutResponse)
    {
        if(signOutObserver != null)
        {
            signOutObserver.signOutRetrieved(signOutResponse);
        }
    }
}
