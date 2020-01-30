package edu.byu.cs.tweeter.model.domain;

import java.net.URL;

public class Link {
    private String linkText;
    private URL linkReference;

    public Link(String linkText)
    {
        this.linkText = linkText;

        //TODO: Parse link text out to create URL.
        this.linkReference = null;
    }

    @Override
    public String toString()
    {
        return linkText;
    }
}
