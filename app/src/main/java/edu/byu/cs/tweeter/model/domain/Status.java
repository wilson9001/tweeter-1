package edu.byu.cs.tweeter.model.domain;

import androidx.core.util.Pair;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Status implements Comparable<Status>
{

    //since java strings have a toString method, and since the other two classes can also implement toString, this may allow for the use of a generic list to print the list.
    private final String statusText;
    private final List<Pair<Integer, Linker>> references;
    private final Date timeStamp;
    private final User poster;

    public Status(@NotNull String statusText, @NotNull User poster)
    {
        this.timeStamp = new Date();
        this.poster = poster;
        this.statusText = statusText;

        //TODO: go through status string and build list of starting indices and references for references

        references = null;
    }

    public String getStatusText()
    {
        return statusText;
    }

    public User getPoster()
    {
        return poster;
    }

    public Date getTimeStamp()
    {
        return new Date(timeStamp.getTime());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(toString());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return toString().equals(status.toString());
    }

    @NotNull
    @Override
    public String toString()
    {
        return "Status{" +
               "alias='" + poster.getAlias() + '\'' +
               ", timestamp='" + timeStamp.getTime() + '\'' +
               ", statusText='" + getStatusText() + '\'' +
               '}';
    }

    @Override
    public int compareTo(Status status)
    {
        return this.timeStamp.compareTo(status.getTimeStamp());
    }
}
