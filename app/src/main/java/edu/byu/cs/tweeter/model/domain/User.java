package edu.byu.cs.tweeter.model.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class User implements Comparable<User>
{

    private final String firstName;
    private final String lastName;
    private final String alias;
    private final String imageUrl;

    public User(@NotNull String firstName, @NotNull String lastName, String imageURL)
    {
        this(firstName, lastName, firstName.concat(lastName), imageURL);
    }

    public User(@NotNull String firstName, @NotNull String lastName, @NotNull String alias, String imageURL)
    {
        this.firstName = firstName.isEmpty() ? "firstName" : firstName;
        this.lastName = lastName.isEmpty() ? "lastName" : lastName;
        this.alias = alias.isEmpty() ? "@".concat(this.firstName).concat(this.lastName) : "@".concat(alias);
        this.imageUrl = imageURL.isEmpty() ? "https://vignette.wikia.nocookie.net/stupididy/images/1/19/Chester_Cheetah.jpg/revision/latest?cb=20161013224811" : imageURL;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getName()
    {
        return String.format("%s %s", firstName, lastName);
    }

    public String getAlias()
    {
        return alias;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return alias.equals(user.alias);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alias);
    }

    @NotNull
    @Override
    public String toString()
    {
        return "User{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", alias='" + alias + '\'' +
               ", imageUrl='" + imageUrl + '\'' +
               '}';
    }

    @Override
    public int compareTo(User user)
    {
        return this.getAlias().compareTo(user.getAlias());
    }
}
