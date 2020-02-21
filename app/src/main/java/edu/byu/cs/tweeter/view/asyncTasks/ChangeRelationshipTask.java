package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.ChangeRelationshipRequest;
import edu.byu.cs.tweeter.net.response.ChangeRelationshipResponse;
import edu.byu.cs.tweeter.presenter.ChangeRelationshipPresenter;

public class ChangeRelationshipTask extends AsyncTask<ChangeRelationshipRequest, Void, ChangeRelationshipResponse>
{
    private final ChangeRelationshipPresenter changeRelationshipPresenter;
    private final ChangeRelationshipObserver changeRelationshipObserver;

    public interface ChangeRelationshipObserver
    {
        public void changeRelationshipRetrieved(ChangeRelationshipResponse changeRelationshipResponse);
    }

    public ChangeRelationshipTask(ChangeRelationshipPresenter changeRelationshipPresenter, ChangeRelationshipObserver changeRelationshipObserver)
    {
        this.changeRelationshipPresenter = changeRelationshipPresenter;
        this.changeRelationshipObserver = changeRelationshipObserver;
    }

    @Override
    protected ChangeRelationshipResponse doInBackground(ChangeRelationshipRequest... changeRelationshipRequests)
    {
        return changeRelationshipPresenter.changeRelationship(changeRelationshipRequests[0]);
    }

    @Override
    protected void onPostExecute(ChangeRelationshipResponse changeRelationshipResponse)
    {
        if(changeRelationshipObserver != null)
        {
            changeRelationshipObserver.changeRelationshipRetrieved(changeRelationshipResponse);
        }
    }
}
