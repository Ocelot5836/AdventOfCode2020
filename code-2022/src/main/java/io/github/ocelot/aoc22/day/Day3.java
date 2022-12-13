package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.data.IntData;
import io.github.ocelot.aoc22.data.StringData;

public class Day3 extends ComputeDay<StringData, IntData>
{
    public Day3()
    {
        super(new IntData(1), "3");
    }

    @Override
    protected StringData processInput(String input)
    {
        return new StringData(input + "\n");
    }
}
