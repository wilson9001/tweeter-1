package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.ChangeRelationshipRequest;
import edu.byu.cs.tweeter.net.response.ChangeRelationshipResponse;

public class ChangeRelationshipService
{
    private static final String changeRelationshipUri = "/changerelationship";
    private static ChangeRelationshipService instance;
    private final ServerFacade serverFacade;

    public static ChangeRelationshipService getInstance()
    {
        if(instance == null)
        {
            instance = new ChangeRelationshipService();
        }

        return instance;
    }

    private ChangeRelationshipService()
    {
        serverFacade = new ServerFacade();
    }

    public ChangeRelationshipResponse changeRelationship(ChangeRelationshipRequest changeRelationshipRequest)
    {
        try
        {
            return serverFacade.changeRelationship(changeRelationshipRequest, changeRelationshipUri);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new ChangeRelationshipResponse("Server error");
        }
    }
}
