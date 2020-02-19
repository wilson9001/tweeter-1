package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.SignUpRequest;
import edu.byu.cs.tweeter.net.response.SignUpResponse;
import edu.byu.cs.tweeter.presenter.SignUpPresenter;

public class SignUpTask extends AsyncTask<SignUpRequest, Void, SignUpResponse>
{
    private final SignUpPresenter signUpPresenter;
    private final SignUpObserver signUpObserver;

    public interface SignUpObserver
    {
        void signUpRetrieved(SignUpResponse signUpResponse);
    }

    public SignUpTask(SignUpPresenter signUpPresenter, SignUpObserver signUpObserver)
    {
        this.signUpPresenter = signUpPresenter;
        this.signUpObserver = signUpObserver;
    }

    @Override
    protected SignUpResponse doInBackground(SignUpRequest... signUpRequests)
    {
        return signUpPresenter.signUp(signUpRequests[0]);
    }

    @Override
    protected void onPostExecute(SignUpResponse signUpResponse)
    {
        if(signUpObserver != null)
        {
            signUpObserver.signUpRetrieved(signUpResponse);
        }
    }
}
