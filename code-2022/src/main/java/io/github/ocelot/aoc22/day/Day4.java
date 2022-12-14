package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.data.IntData;
import io.github.ocelot.aoc22.data.StringData;

public class Day4 extends ComputeDay<StringData>
{
    public Day4()
    {
        super(new IntData(1), "4");
    }

    @Override
    protected StringData processInput(String input)
    {
        return new StringData(input + "\n");
    }
}
