package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ocelot
 */
public class Day5
{
    public static void main(String[] args) throws IOException
    {
        TimeTracker tracker = new TimeTracker();

        // Parse input data
        tracker.startSection("parse");
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day5.class.getResourceAsStream("/5/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }
        String[] input = Arrays.stream(data.toString().split(",")).toArray(String[]::new);

        // Process each line as a boarding pass
        tracker.endStartSection("validate");
        int[] result = new int[input.length];
        for (int i = 0; i < input.length; i++)
        {
            String pass = input[i];
            if (pass.length() != 10) // This should never be thrown
                throw new IllegalArgumentException("Invalid boarding pass '" + pass + "'");

            int row = parse(pass.substring(0, 7)); // Parse row from first 7 chars
            int col = parse(pass.substring(7, 10)); // Parse col from last 3 chars

            result[i] = row * 8 + col; // Generate id from row and col
        }

        // Print highest seat id
        Arrays.stream(result).max().ifPresent(max -> System.out.println("Highest seat id is " + max));

        // Create a set of all adjacent seat ids
        tracker.endStartSection("process");
        Set<Integer> values = new HashSet<>();
        Arrays.stream(result).forEach(value ->
        {
            values.add(value - 1);
            values.add(value + 1);
        });
        // Remove all seat ids in the set
        values.removeIf(value -> Arrays.stream(result).anyMatch(s -> value == s));
        tracker.endSection();

        // Print second result
        System.out.println("Missing seats are " + values);
        System.out.println(tracker);
    }

    /**
     * Parses a binary tree from the input data.
     *
     * @param input The input data to parse
     * @return A single number that was parsed from the binary operation
     */
    private static int parse(String input)
    {
        // Generate initial range from the input data
        IntRange range = new IntRange(0, (int) Math.pow(2, input.length()) - 1);

        // Loop through input to split range into upper and lower until a single number is found
        for (int i = 0; i < input.length(); i++)
        {
            double median = range.getMedian();
            switch (input.charAt(i))
            {
                case 'F', 'L' -> range = new IntRange(range.getMin(), (int) Math.floor(median)); // Lower half
                case 'B', 'R' -> range = new IntRange((int) Math.ceil(median), range.getMax()); // Upper half
                default -> throw new IllegalStateException("Invalid segment '" + input.charAt(i) + "'"); // This should never be thrown
            }
        }
        if (range.getMin() != range.getMax()) // This should never be thrown
            throw new IllegalStateException("Range did not become a single number. Was " + range);
        return range.getMin(); // min is the same as max so it doesn't matter which is returned
    }
}
