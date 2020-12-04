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
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day4.class.getResourceAsStream("/4/input.txt"))))
        {
            while (reader.ready())
            {
                String line = reader.readLine().trim();
                if (line.isEmpty())
                {
                    data.append(",");
                }
                else
                {
                    data.append(line).append(" ");
                }
            }
        }

        String[] input = data.toString().split(",");
        System.out.println(input.length);

        int passportCount = 0;
        for (String passport : input)
        {
            String[] fields = passport.split(" ");
            if (fields.length < REQUIRED_FIELDS.size())
                continue;

            byte present = 0;
            fields:
            for (String field : fields)
            {
                String[] pair = field.trim().split(":");
                if (pair.length != 2)
                    throw new IllegalArgumentException("Invalid pair '" + field + "'");
                String key = pair[0];
                for (int i = 0; i < FIELDS.size(); i++)
                {
                    PassportField passportField = FIELDS.get(i);
                    if (passportField.getName().equals(key))
                    {
                        present |= (passportField.test(pair[1]) ? 1 : 0) << i;
                        continue fields;
                    }
                }
                throw new IllegalArgumentException("Unknown key '" + key + "'");
            }

            boolean valid = true;
            for (int i = 0; i < REQUIRED_FIELDS.size(); i++)
            {
                if (((present >> i) & 1) == 0)
                {
                    valid = false;
                    break;
                }
            }
            if (valid)
                passportCount++;
        }
        System.out.println("There are " + passportCount + " valid passports");
    }
}
