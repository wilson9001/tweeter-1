package edu.byu.cs.tweeter.presenter;

//TODO: Create net and model classes and import them here

import edu.byu.cs.tweeter.model.services.FollowersService;
import edu.byu.cs.tweeter.net.request.FollowersRequest;
import edu.byu.cs.tweeter.net.response.FollowersResponse;

public class FollowersPresenter extends Presenter {

    private final View view;

    public interface View{
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public FollowersPresenter(View view) { this.view = view; }

    public FollowersResponse getFollowers(FollowersRequest followersRequest)
    {
        return FollowersService.getInstance().getFollowers(followersRequest);
    }
}
