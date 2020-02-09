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
 * A temporary class that generates {@link Status} objects. This class may be removed when the server is created and the ServerFacade no longer needs to return dummy data.
 */

public class StatusGenerator
{
    private static final List<String> words;
    private static final int maxWordCount = 280;
    private static final int maxParagraphs = 3;
    private static final int totalWords = 466441;

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

    static
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
    }

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

        //determine word count
        int wordCount;

        do
        {
            wordCount = random.nextInt(maxWordCount + 1);

        } while (wordCount == 0);


        //determine number of paragraphs
        int paragraphCount;

        do
        {
            paragraphCount = random.nextInt(maxParagraphs + 1);

        } while (paragraphCount == 0);

        //determine number of words in each paragraph
        int[] wordCountByParagraph = new int[paragraphCount];

        int wordsRemaining = wordCount;

        for (int paragraphIndex = 0; paragraphIndex < paragraphCount; paragraphIndex++)
        {
            if ((paragraphIndex + 1) < paragraphCount)
            {
                int wordsInParagraph;

                do
                {
                    wordsInParagraph = random.nextInt((wordsRemaining - (paragraphCount - (paragraphIndex + 1)))+ 1);

                } while (wordsInParagraph == 0);

                wordCountByParagraph[paragraphIndex] =  wordsInParagraph;

                wordsRemaining -= wordsInParagraph;
            }
            else
            {
                wordCountByParagraph[paragraphCount - 1] = wordsRemaining;
            }
        }

        //TODO: create list of random words for each paragraph
        StringBuilder stringBuilder = new StringBuilder();

        for (int paragraphIndex = 0; paragraphIndex < paragraphCount; paragraphIndex++)
        {
            for (int currentWordCount = 0 ; currentWordCount < wordCountByParagraph[paragraphIndex]; currentWordCount++)
            {
                //probabilistically add random word or punctuation
                stringBuilder.append(words.get(random.nextInt(wordCount)));
                stringBuilder.append(" ");
            }

            if ((paragraphIndex + 1) < paragraphCount)
            {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }
}