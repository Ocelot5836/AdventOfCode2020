package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author Ocelot
 */
public class Day9
{
    public static void main(String[] args) throws IOException
    {
        TimeTracker tracker = new TimeTracker();

        // Parse input data
        tracker.startSection("parse");
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day9.class.getResourceAsStream("/9/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }
        long[] input = Arrays.stream(data.toString().split(",")).mapToLong(Long::parseLong).toArray();

        tracker.endStartSection("identify");
        long[] buffer = new long[25];
        long invalid = 0;
        loop:
        for (int i = buffer.length; i < input.length; i++)
        {
            System.arraycopy(input, i - buffer.length, buffer, 0, buffer.length);
            long value = input[i];

            for (int j = 0; j < buffer.length * buffer.length; j++)
            {
                if (j % buffer.length == j / buffer.length)
                    continue;
                if (buffer[j % buffer.length] + buffer[j / buffer.length] == value)
                    continue loop;
            }
            invalid = value;
            break;
        }
        tracker.endStartSection("process");
        long result = 0;
        loop:
        for (int i = 0; i < input.length; i++)
        {
            long counter = 0;
            int j = 0;
            while (counter < invalid && j - i < input.length)
            {
                counter += input[i + j];
                if (counter == invalid)
                {
                    long[] results = new long[j + 1];
                    System.arraycopy(input, i, results, 0, j + 1);
                    result = Arrays.stream(results).min().orElseThrow(IllegalStateException::new) + Arrays.stream(results).max().orElseThrow(IllegalStateException::new);
                    break loop;
                }
                j++;
            }
        }
        tracker.endSection();

        // Print result
        System.out.println("First number that cannot be added is " + invalid + ", sum of contiguous set is " + result);
        System.out.println(tracker);
    }
}
