package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Day3
{
    public static void main(String[] args) throws IOException
    {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day3.class.getResourceAsStream("/3/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }

        String[] input = data.toString().split(",");
        System.out.println(input.length);
        int width = input[0].length(); // Assumed to be the entire width of a square
        int[] treeCount = new int[5];
        for (int func = 0; func < treeCount.length; func++)
        {
            System.out.println("Using function " + func + ": Right " + scaleX(func) + ", Down " + scaleY(func));

            int i = 0;
            int y = 0;
            while (y < input.length)
            {
                int x = (scaleX(func) * i) % width;
                if (input[y].charAt(x) == '#')
                    treeCount[func]++;
                y += scaleY(func);
                i++;
            }
        }

        long product = 1;
        for (int count : treeCount)
            product *= count;
        System.out.println(Arrays.toString(treeCount));
        System.out.println("There are " + product + " trees in all multiplied path");
    }

    private static int scaleX(int function)
    {
        return function == 4 ? 1 : function * 2 + 1;
    }

    private static int scaleY(int function)
    {
        return function == 4 ? 2 : 1;
    }
}
