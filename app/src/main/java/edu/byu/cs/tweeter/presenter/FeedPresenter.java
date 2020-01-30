package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.FeedService;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;

public class FeedPresenter extends Presenter {
    private final View view;

    public interface View{
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public FeedPresenter(View view) { this.view = view; }

    public FeedResponse getFeed(FeedRequest feedRequest)
    {
        return FeedService.getInstance().getFeed(feedRequest);
    }
}
