package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.data.FloatData;

import java.nio.ByteBuffer;

public class Day0 extends ComputeDay<FloatData, FloatData>
{
    public Day0()
    {
        super(Float.BYTES, "0");
    }

    @Override
    protected FloatData processInput(String input)
    {
        return new FloatData(Float.parseFloat(input), 2, 3, 4, 5);
    }

    @Override
    protected void processOutput(FloatData output)
    {
        this.logger.info("Got " + output);
    }

    @Override
    protected FloatData create(ByteBuffer buffer)
    {
        return new FloatData(1, buffer);
    }
}
