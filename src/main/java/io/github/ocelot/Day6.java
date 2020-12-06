package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Ocelot
 */
public class Day6
{
    public static void main(String[] args) throws IOException
    {
        // Parse input data
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day4.class.getResourceAsStream("/6/input.txt"))))
        {
            while (reader.ready())
            {
                String line = reader.readLine().trim();
                // If the line is empty it is assumed to be the end of the group
                if (line.isEmpty())
                {
                    data.append(",");
                }
                else
                {
                    // Append lines together to unify all responses in a group
                    data.append(line).append(" ");
                }
            }
        }
        String[] input = data.toString().split(",");

        // Process each group of responses
        long count = 0;
        for (String group : input)
        {
            // Individual responses need to be parsed from a group
            String[] responses = group.split(" ");
            // An array of integers for each response. Each int is essentially 32 booleans, the first 26 of which are used
            int[] flags = new int[responses.length];

            // Loop through each response in the group
            for (int i = 0; i < responses.length; i++)
            {
                String response = responses[i];
                // Loop through each question answered 'yes' and set the flags accordingly. 'a' is subtracted since it will shift a-z characters to 0 instead of 97
                for (int j = 0; j < response.length(); j++)
                    flags[i] |= 1 << response.charAt(j) - 'a';
            }

            // The max value of an integer is all bits set so it's used to AND all unset bits away
            int flag = Integer.MAX_VALUE;
            for (int f : flags)
                flag &= f; // For each flag for each response, AND with the cumulative flag to unset any bits that are not in this flag

            // Loop through all bits in the integers
            for (int i = 0; i < Integer.SIZE; i++)
                if (((flag >> i) & 1) == 1) // If the flag is set then it must have been set for all responses to increment the count accordingly
                    count++;
        }

        // Print result
        System.out.println("Overall, " + count + " questions were answered yes.");
    }
}
