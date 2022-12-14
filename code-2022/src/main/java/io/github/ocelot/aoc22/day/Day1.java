package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.data.IntData;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Day1 extends ComputeDay<IntData>
{
    public Day1()
    {
        super(new IntData(1), "1");
    }

    @Override
    protected IntData processInput(String input)
    {
        int[] data = Arrays.stream(input.split("\n")).mapToInt(s -> s.isBlank() ? -1 : Integer.parseInt(s)).toArray();
        return new IntData(data);
    }
}
