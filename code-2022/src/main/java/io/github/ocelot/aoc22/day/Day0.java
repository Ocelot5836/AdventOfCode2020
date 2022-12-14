package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.data.FloatData;

public class Day0 extends ComputeDay<FloatData>
{
    public Day0()
    {
        super(new FloatData(1), "0");
    }

    @Override
    protected FloatData processInput(String input)
    {
        return new FloatData(Float.parseFloat(input), 2, 3, 4, 5);
    }
}
