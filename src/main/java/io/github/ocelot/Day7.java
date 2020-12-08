package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ocelot
 */
public class Day7
{
    public static void main(String[] args) throws IOException
    {
        TimeTracker tracker = new TimeTracker();

        // Parse input data
        tracker.startSection("parse");
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day7.class.getResourceAsStream("/7/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append("\n");
        }
        String[] input = data.toString().split("\n");

        // Loop through each line as a single airport bag
        tracker.endStartSection("process");
        Map<String, AirportBag> bags = new HashMap<>();
        for (String rule : input)
        {
            // Split the words out for processing
            String[] words = rule.split(" ");

            // Phase is used to process the individual sections
            int phase = 0;
            StringBuilder identifier = new StringBuilder();
            int count = 0;
            AirportBag parent = null;
            for (String word : words)
            {
                switch (phase)
                {
                    // First half of the line
                    case 0, 1 -> {
                        if (word.equalsIgnoreCase("bags") || word.equalsIgnoreCase("contain"))
                        {
                            phase++;
                            continue;
                        }
                        identifier.append(word);
                    }
                    // Second half repeating
                    case 2 -> {
                        if (parent == null)
                        {
                            parent = bags.computeIfAbsent(identifier.toString(), key -> new AirportBag(key, 1));
                            identifier = new StringBuilder();
                        }
                        if (count == 0)
                        {
                            if (word.equalsIgnoreCase("no"))
                            {
                                phase++;
                                continue;
                            }
                            count = Integer.parseInt(word);
                            continue;
                        }
                        if (word.startsWith("bag"))
                        {
                            parent.addChild(new AirportBag(identifier.toString(), count));
                            count = 0;
                            identifier = new StringBuilder();
                            continue;
                        }
                        identifier.append(word);
                    }
                    // Skip rest of line
                    case 3 -> {
                    }
                }
            }
        }
        tracker.endStartSection("tally");

        // Tally up how many gold bags are in all bags besides gold
        int count = bags.values().stream().mapToInt(bag ->
        {
            if (bag.getIdentifier().equals("shinygold"))
                return 0;
            for (AirportBag childBag : bag.getChildren())
                if (childBag.getIdentifier().equals("shinygold"))
                    return 1;
            List<AirportBag> processing = new ArrayList<>(bag.getChildren());
            while (!processing.isEmpty())
            {
                AirportBag child = bags.get(processing.remove(0).getIdentifier());
                if (child.getIdentifier().equals("shinygold"))
                {
                    return 1;
                }
                else
                {
                    for (AirportBag childBag : child.getChildren())
                    {
                        if (childBag.getIdentifier().equals("shinygold"))
                        {
                            return 1;
                        }
                    }
                    processing.addAll(child.getChildren());
                }
            }
            return 0;
        }).sum();

        // Tally up how many bags are inside a single gold bag
        int storageCount = 0;
        List<AirportBag> processing = new ArrayList<>(bags.get("shinygold").getChildren());
        while (!processing.isEmpty())
        {
            AirportBag bag = processing.remove(0);
            if (bag.getIdentifier().equals("shinygold"))
                throw new IllegalStateException("Recursion should not occur!");

            storageCount += bag.getCount();
            for (int i = 0; i < bag.getCount(); i++)
                processing.addAll(bags.get(bag.getIdentifier()).getChildren());
        }
        tracker.endSection();

        // Print result
        System.out.println(count + " shiny gold bags can be contained. " + storageCount + " can be contained in a gold bag");
        System.out.println(tracker);
    }
}
