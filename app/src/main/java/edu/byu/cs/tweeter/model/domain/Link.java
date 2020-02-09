package edu.byu.cs.tweeter.model.domain;

import android.util.Log;

public class Link implements Linker
{
    private String linkText;
    private String url;

    public Link(String linkText, String url)
    {
        this.linkText = linkText;
        this.url = url;
    }

    @Override
    public String toString()
    {
        return linkText;
    }

    @Override
    public String getReference()
    {
        return url;
    }

    @Override
    public void activateReference()
    {
        Log.d("Link.activateReference", "You clicked on a link to: " + url);
    }
}
