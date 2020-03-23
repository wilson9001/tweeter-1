package edu.byu.cs.tweeter.net.response;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.SimpleStatus;
import edu.byu.cs.tweeter.model.domain.Status;

public class FeedResponse_Simple extends PagedResponse
{
    public List<SimpleStatus> statuses;

    public FeedResponse_Simple(String message)
    {
        super(false, message, false);
    }

    public FeedResponse_Simple(List<SimpleStatus> statuses, boolean hasMorePages)
    {
        super(true, hasMorePages);
        this.statuses = statuses;
    }

    public List<SimpleStatus> getStatuses()
    {
        return statuses;
    }

    public FeedResponse toFeedResponse()
    {
        if(getMessage() == null)
        {
            List<Status> newStatuses = new ArrayList<>(statuses.size());

            for(SimpleStatus status : statuses)
            {
                newStatuses.add(new Status(status.statusText, status.poster, status.timeStamp));
            }

            return new FeedResponse(newStatuses, hasMorePages());
        }

        return new FeedResponse(getMessage());
    }
}
