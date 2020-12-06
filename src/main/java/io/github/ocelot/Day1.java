package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author Ocelot
 */
public class Day1
{
    public static void main(String[] args) throws IOException
    {
        TimeTracker tracker = new TimeTracker();

        // Parse input data
        tracker.startSection("parse");
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day1.class.getResourceAsStream("/1/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }
        int[] input = Arrays.stream(data.toString().split(",")).mapToInt(Integer::parseInt).toArray();

        // Process each line n^2 times
        tracker.endStartSection("process");
        for (int i = 0; i < input.length * input.length; i++)
        {
            // Process each number value for each iteration
            for (int value : input)
            {
                // Extract x and y values
                int x = i % input.length;
                int y = i / input.length;
                // Check if all three values add up to 2020
                if (value + input[x] + input[y] == 2020)
                {
                    tracker.endSection();

                    // Print result
                    System.out.println(value * input[x] * input[y]);
                    System.out.println(tracker);
                    return;
                }
            }
        }
    }
}
