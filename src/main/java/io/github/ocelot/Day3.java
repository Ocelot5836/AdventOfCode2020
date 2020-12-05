package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author Ocelot
 */
public class Day3
{
    public static void main(String[] args) throws IOException
    {
        // Parse input data
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day3.class.getResourceAsStream("/3/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }
        String[] input = data.toString().split(",");

        int width = input[0].length(); // Assumed to be the width of the entire square
        int[] treeCount = new int[5]; // There are 5 functions, process each for all position until the bottom is reached
        for (int func = 0; func < treeCount.length; func++)
        {
            // i and y are tracked separately in order to validate x is the correct value
            int i = 0;
            int y = 0;
            while (y < input.length)
            {
                int x = (scaleX(func) * i) % width;
                // If the character at the pointer is a tree, mark it down under the current function
                if (input[y].charAt(x) == '#')
                    treeCount[func]++;
                y += scaleY(func);
                i++;
            }
        }

        // Print result
        Arrays.stream(treeCount).asLongStream().reduce((a, b) -> a * b).ifPresent(product -> System.out.println("There are " + product + " trees in the multiplied path"));
    }

    /**
     * Creates a scale for the provided function value.
     *
     * @param function The function to use
     * @return A scale factor for the x component
     */
    private static int scaleX(int function)
    {
        return function == 4 ? 1 : function * 2 + 1;
    }

    /**
     * Creates a scale for the provided function value.
     *
     * @param function The function to use
     * @return A scale factor for the y component
     */
    private static int scaleY(int function)
    {
        return function == 4 ? 2 : 1;
    }
}
