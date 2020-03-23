package edu.byu.cs.tweeter.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * A temporary class that generates {@link Status} objects. This class may be removed when the server is created and the LocalServerFacade no longer needs to return dummy data.
 */

public class StatusGenerator
{
    //private static final List<String> words;
    private static final int maxCharCount = 280;
    private static final int maxWordLength = 10;
    private static final int minWordLength = 1;
    //private static final int totalWords = 466441;

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

    /*static
    {
        words = new ArrayList<>(totalWords);
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
            String word;

            while((word = reader.readLine()) != null)
            {
                words.add(word);
            }
            reader.close();
        }
        catch (Exception exception)
        {
            Log.e("StatusGenerator-static", exception.toString(), exception);
        }
    }*/

    public List<Status> generateStatuses(int minStatuses, int maxStatuses, Set<User> users)
    {
        List<Status> statuses = new LinkedList<>();
        Random random = new Random();

        //for each user...
        for (User user : users)
        {
            int totalStatusCount;

            do
            {
                totalStatusCount = random.nextInt(maxStatuses + 1);

            } while (totalStatusCount < minStatuses);

            //for each status...
            for (int statusCount = 0; statusCount < totalStatusCount; statusCount++)
            {
                statuses.add(new Status(generateStatusText(), user));
            }
        }

        return statuses;
    }

    private String generateStatusText()
    {
        Random random = new Random();

        //determine char count
        int charCount;

        do
        {
            charCount = random.nextInt(maxCharCount + 1);

        } while (charCount == 0);


        int charsRemaining = charCount;

        StringBuilder builder = new StringBuilder();

        do
        {
            int nextWordLength = generateWordLength(charsRemaining < maxWordLength ? charsRemaining : maxWordLength);

            for (int currentWordLength = 0; currentWordLength < nextWordLength; currentWordLength++)
            {
                builder.append((char)(random.nextInt(26) + 'a'));
            }

            charsRemaining -= nextWordLength;

            if(charsRemaining > 0)
            {
                builder.append(" ");
            }

            --charsRemaining;

        } while(charsRemaining > 0);

        return builder.toString();
    }

    private int generateWordLength(int maxLength)
    {
        Random random = new Random();
        int wordLength;

        do
        {
            wordLength = random.nextInt(maxLength + 1);

        } while (wordLength < minWordLength);

        return wordLength;
    }
}