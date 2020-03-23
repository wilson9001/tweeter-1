package edu.byu.cs.tweeter.net.request;

import edu.byu.cs.tweeter.model.domain.SimpleStatus;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryRequest
{
    public User owner;
    public int limit;
    public SimpleStatus lastStatus;

    public StoryRequest(User owner, int limit, Status lastStatus)
    {
        this.owner = owner;
        this.limit = limit;
        this.lastStatus = lastStatus == null ? null : new SimpleStatus(lastStatus.getStatusText(), lastStatus.getPoster(), lastStatus.getRawTimeStamp());
    }

    public User getOwner()
    {
        return owner;
    }

    public int getLimit()
    {
        return limit;
    }

    public Status getLastStatus()
    {
        return new Status(lastStatus.statusText, lastStatus.poster, lastStatus.timeStamp);
    }
}
