package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Day1
{
    public static void main(String[] args) throws IOException
    {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day1.class.getResourceAsStream("/1/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }

        int[] input = Arrays.stream(data.toString().split(",")).mapToInt(Integer::parseInt).toArray();

        for (int i = 0; i < input.length * input.length; i++)
        {
            for (int value : input)
            {
                if (value + input[i / input.length] + input[i % input.length] == 2020)
                {
                    System.out.println(value * input[i / input.length] * input[i % input.length]);
                    return;
                }
            }
        }
    }
}
