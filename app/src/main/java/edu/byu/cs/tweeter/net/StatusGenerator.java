package edu.byu.cs.tweeter.net;

public class StatusGenerator
{
    private static StatusGenerator instance;

    private StatusGenerator()
    {
    }

    public static StatusGenerator getInstance()
    {
        if (instance == null)
        {
            instance = new StatusGenerator();
        }

        return instance;
    }
}
