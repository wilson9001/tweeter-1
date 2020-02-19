package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.SignInRequest;
import edu.byu.cs.tweeter.net.response.SignInResponse;
import edu.byu.cs.tweeter.presenter.SignInPresenter;

public class SignInTask extends AsyncTask<SignInRequest, Void, SignInResponse>
{
    private final SignInPresenter signInPresenter;
    private final SignInObserver signInObserver;

    public interface SignInObserver
    {
        void signInRetrieved(SignInResponse signInResponse);
    }

    public SignInTask(SignInPresenter signInPresenter, SignInObserver signInObserver)
    {
        this.signInObserver = signInObserver;
        this.signInPresenter = signInPresenter;
    }

    @Override
    protected SignInResponse doInBackground(SignInRequest... signInRequests)
    {
        return signInPresenter.signIn(signInRequests[0]);
    }

    @Override
    protected void onPostExecute(SignInResponse signInResponse)
    {
        if(signInObserver != null)
        {
            signInObserver.signInRetrieved(signInResponse);
        }
    }
}
