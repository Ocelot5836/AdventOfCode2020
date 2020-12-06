package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * @author Ocelot
 */
public class Day2
{
    private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

    public static void main(String[] args) throws IOException
    {
        TimeTracker tracker = new TimeTracker();

        // Parse input data
        tracker.startSection("parse");
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day2.class.getResourceAsStream("/2/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }
        String[] input = data.toString().split(",");

        // Process each line
        tracker.endStartSection("process");
        int validPolicies = 0;
        passwords:
        for (String entry : input)
        {
            String[] parts = entry.split(" ");
            if (parts.length != 3) // All entries must have a length of 3. This should never be thrown.
                throw new IllegalArgumentException("Invalid entry '" + entry + "'");

            int[] positions = parsePositions(parts[0]);
            char token = parts[1].charAt(0);
            String password = parts[2];

            // Loop through the two positions defined in the data set
            boolean flag = false;
            for (int position : positions)
            {
                if (password.charAt(position) == token)
                {
                    if (flag) // If it already has the token then this must be a duplicate which is invalid
                        continue passwords;
                    // Mark the token as found
                    flag = true;
                }
            }

            if (!flag) // If no tokens were found
                continue;

            // Increment valid policies since there was only 1 of the requested token in either position
            validPolicies++;
        }
        tracker.endSection();

        // Print result
        System.out.println(validPolicies + " policies were valid out of " + input.length + ". (" + FORMAT.format((double) validPolicies / (double) input.length * 100.0) + "% valid)");
        System.out.println(tracker);
    }

    /**
     * Parses the set of <code>#-#</code> into an array of two integers.
     *
     * @param input The input data
     * @return The array of all position integers parsed
     */
    private static int[] parsePositions(String input)
    {
        String[] parts = input.split("-");
        if (parts.length != 2)
            throw new IllegalArgumentException("Range must be two parts");
        return new int[]{Integer.parseInt(parts[0].trim()) - 1, Integer.parseInt(parts[1].trim()) - 1};
    }
}
