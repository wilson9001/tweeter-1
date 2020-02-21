package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.ChangeRelationshipService;
import edu.byu.cs.tweeter.net.request.ChangeRelationshipRequest;
import edu.byu.cs.tweeter.net.response.ChangeRelationshipResponse;

public class ChangeRelationshipPresenter extends Presenter
{
    private final View view;

    public interface View
    {

    }

    public ChangeRelationshipPresenter(View view)
    {
        this.view = view;
    }

    public ChangeRelationshipResponse changeRelationship(ChangeRelationshipRequest changeRelationshipRequest)
    {
        return ChangeRelationshipService.getInstance().changeRelationship(changeRelationshipRequest);
    }
}
