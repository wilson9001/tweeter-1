package edu.byu.cs.tweeter.net.request;

public class FeedRequest_Net
{
    public String authToken;
    public FeedRequest request;

    public FeedRequest_Net()
    {}

    public FeedRequest_Net(String authToken, FeedRequest request)
    {
        this.authToken = authToken;
        this.request = request;
    }
}
