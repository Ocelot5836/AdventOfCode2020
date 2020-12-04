package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class Day2
{
    private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

    public static void main(String[] args) throws IOException
    {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day2.class.getResourceAsStream("/2/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }

        String[] input = data.toString().split(",");

        int validPolicies = 0;
        passwords:
        for (String entry : input)
        {
            String[] parts = entry.split(" ");
            if (parts.length != 3)
                throw new IllegalArgumentException("Invalid entry '" + entry + "'");

            int[] positions = parsePositions(parts[0]);
            char token = parts[1].charAt(0);
            String password = parts[2];
            boolean flag = false;
            for (int position : positions)
            {
                if (password.charAt(position) == token)
                {
                    if (flag)
                        continue passwords;
                    flag = true;
                }
            }
            if (!flag)
                continue;
            validPolicies++;
        }
        System.out.println(validPolicies + " policies were valid out of " + input.length + ". (" + FORMAT.format((double) validPolicies / (double) input.length * 100.0) + "% valid)");
    }

    private static int[] parsePositions(String input)
    {
        String[] parts = input.split("-");
        if (parts.length != 2)
            throw new IllegalArgumentException("Range must be two parts");
        return new int[]{Integer.parseInt(parts[0].trim()) - 1, Integer.parseInt(parts[1].trim()) - 1};
    }
}
