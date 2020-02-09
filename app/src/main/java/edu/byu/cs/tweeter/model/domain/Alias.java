package edu.byu.cs.tweeter.model.domain;

import android.util.Log;
import android.widget.Toast;

public class Alias implements Linker
{
    private String alias;

    public Alias(String handleText)
    {
        this.alias = handleText;
    }

    @Override
    public String getReference()
    {
        return alias;
    }

    @Override
    public void activateReference()
    {
        //TODO: Switch to view of person referenced in alias
        Log.d("Alias.activateReference", "You selected a reference to: " + alias);
    }

    @Override
    public String toString()
    {
        return "@" + alias;
    }
}
