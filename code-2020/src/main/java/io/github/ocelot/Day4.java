package io.github.ocelot;

import io.github.ocelot.passport.PassportField;
import io.github.ocelot.passport.PassportValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.ocelot.passport.PassportValidator.*;

/**
 * @author Ocelot
 */
public class Day4
{
    private static final List<PassportField> REQUIRED_FIELDS = Arrays.asList(
            new PassportField("byr", and(length(4), range(1920, 2002))),
            new PassportField("iyr", and(length(4), range(2010, 2020))),
            new PassportField("eyr", and(length(4), range(2020, 2030))),
            new PassportField("hgt", or(measurement(150, 193, "cm"), measurement(59, 76, "in"))),
            new PassportField("hcl", regex("^(#)([0-9,a-f]{6})")),
            new PassportField("ecl", regex("\\bamb\\b|\\bblu\\b|\\bbrn\\b|\\bgry\\b|\\bgrn\\b|\\bhzl\\b|\\both\\b")),
            new PassportField("pid", regex("\\b([0-9]{9})\\b"))
    );
    private static final List<String> OPTIONAL_FIELDS = Collections.singletonList("cid");
    private static final List<PassportField> FIELDS = Stream.concat(REQUIRED_FIELDS.stream(), OPTIONAL_FIELDS.stream().map(s -> new PassportField(s, PassportValidator.ALWAYS_TRUE))).collect(Collectors.toList());

    public static void main(String[] args) throws IOException
    {
        TimeTracker tracker = new TimeTracker();

        // Parse input data
        tracker.startSection("parse");
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day4.class.getResourceAsStream("/4/input.txt"))))
        {
            while (reader.ready())
            {
                String line = reader.readLine().trim();
                // If the line is empty the end of a passport is assumed
                if (line.isEmpty())
                {
                    data.append(",");
                }
                else
                {
                    // Append lines together to unify it into a single line split by spaces
                    data.append(line).append(" ");
                }
            }
        }
        String[] input = data.toString().split(",");

        // Process each line as a passport
        tracker.endStartSection("process");
        int passportCount = 0;
        for (String passport : input)
        {
            String[] fields = passport.split(" ");
            // If there aren't enough fields to fill the required size, it must be impossible to be valid
            if (fields.length < REQUIRED_FIELDS.size())
                continue;

            // Store status in a single 8 bit number. There are 8 fields to it works out
            byte present = 0;
            fields:
            for (String field : fields)
            {
                String[] pair = field.trim().split(":");
                if (pair.length != 2) // This should never be thrown
                    throw new IllegalArgumentException("Invalid pair '" + field + "'");

                String key = pair[0];
                for (int i = 0; i < FIELDS.size(); i++)
                {
                    PassportField passportField = FIELDS.get(i);
                    if (passportField.getName().equals(key)) // If key is found in fields, process it
                    {
                        present |= (passportField.test(pair[1]) ? 1 : 0) << i; // Mark field as present if the test passed
                        continue fields;
                    }
                }
                // This should never be called
                throw new IllegalArgumentException("Unknown key '" + key + "'");
            }

            // Loop through all fields to check if a required field is missing it's flag
            boolean valid = true;
            for (int i = 0; i < REQUIRED_FIELDS.size(); i++)
            {
                if (((present >> i) & 1) == 0) // If the value is 0, the test from before failed and the passport is no longer valid
                {
                    // Mark as invalid and stop checking for validity
                    valid = false;
                    break;
                }
            }
            if (valid) // If valid is not false, all checks passed and the count is incremented
                passportCount++;
        }
        tracker.endSection();

        // Print result
        System.out.println("There are " + passportCount + " valid passports");
        System.out.println(tracker);
    }
}
