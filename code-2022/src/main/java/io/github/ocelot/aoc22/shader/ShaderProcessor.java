package io.github.ocelot.aoc22.shader;

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
                    line = line.substring(0, start) + "int[](" + IntStream.concat(IntStream.of(sequence.length()), sequence.chars()).mapToObj(Integer::toString).collect(Collectors.joining(", ")) + ")" + line.substring(end + 1);
                }
            }

            lines[i] = line;
        }
        return lines;
    }
}
