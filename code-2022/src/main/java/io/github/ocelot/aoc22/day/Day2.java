package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.data.IntData;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day2 extends ComputeDay<IntData>
{
    public Day2()
    {
        super(new IntData(1), "2");
    }

    @Override
    protected IntData processInput(String input)
    {
        int[] data = Arrays.stream(input.split("\n")).flatMapToInt(s ->
        {
            String[] parts = s.split(" ");
            return IntStream.of(parts[0].charAt(0) - 'A', parts[1].charAt(0) - 'X');
        }).toArray();
        return new IntData(data);
    }
}
