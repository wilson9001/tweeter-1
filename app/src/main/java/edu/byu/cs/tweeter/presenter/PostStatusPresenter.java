package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.PostStatusService;
import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.response.PostStatusResponse;

public class PostStatusPresenter extends Presenter
{
    private final View view;

    public interface View
    {

    }

    public PostStatusPresenter(View view)
    {
        this.view = view;
    }

    public PostStatusService getService()
    {
        return PostStatusService.getInstance();
    }

    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest)
    {
        return getService().postStatus(postStatusRequest);
    }
}
