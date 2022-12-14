package io.github.ocelot.aoc22.shader;

import io.github.ocelot.aoc22.data.StringData;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShaderProcessor
{
    public static String[] processSource(String[] lines)
    {
        for (int i = 0; i < lines.length; i++)
        {
            String line = lines[i];
            if (!line.contains("\""))
            {
                continue;
            }

            int start = -1;
            for (int end = 0; end < line.length(); end++)
            {
                if (line.charAt(end) == '"')
                {
                    if (start == -1)
                    {
                        start = end;
                        continue;
                    }

                    String sequence = line.substring(start + 1, end);
                    if (sequence.length() > StringData.MAX_SIZE)
                    {
                        throw new IllegalStateException("Max string length is " + StringData.MAX_SIZE);
                    }
                    line = line.substring(0, start) + "int[](" + IntStream.concat(sequence.chars(), IntStream.of(0)).mapToObj(Integer::toString).collect(Collectors.joining(", ")) + ")" + line.substring(end + 1);
                }
            }

            lines[i] = line;
        }
        return lines;
    }
}
