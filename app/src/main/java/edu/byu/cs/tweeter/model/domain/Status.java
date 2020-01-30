package edu.byu.cs.tweeter.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Status {

    //since java strings have a toString method, and since the other two classes can also implement toString, this may allow for the use of a generic list to print the list.
    private List statusChunks;

    public Status(List statusChunks)
    {
        this.statusChunks = statusChunks;
    }

    public String getStatusText()
    {
        StringBuilder builder = new StringBuilder();

        for (Object statusChunk : statusChunks)
        {
            builder.append(statusChunk.toString());
        }

        return builder.toString();
    }
}
