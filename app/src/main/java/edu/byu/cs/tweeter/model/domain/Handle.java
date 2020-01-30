package edu.byu.cs.tweeter.model.domain;

public class Handle {
    private String handleText;
    private User userReference;

    public Handle(String handleText, User userReference)
    {
        this.handleText = handleText;
        this.userReference = userReference;
    }

    @Override
    public String toString()
    {
        return handleText;
    }

    public User getUser()
    {
        return userReference;
    }
}
