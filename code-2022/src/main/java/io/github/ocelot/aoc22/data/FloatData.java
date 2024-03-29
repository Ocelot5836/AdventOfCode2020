package io.github.ocelot.aoc22.data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public record FloatData(float... values) implements DayData
{
    public FloatData(int size)
    {
        this(new float[size]);
    }

    @Override
    public String toString()
    {
        return Arrays.toString(this.values);
    }

    @Override
    public void read(ByteBuffer buffer)
    {
        buffer.asFloatBuffer().get(this.values);
    }

    @Override
    public void write(ByteBuffer buffer)
    {
        buffer.asFloatBuffer().put(this.values);
    }

    @Override
    public int size()
    {
        return this.values.length * Float.BYTES;
    }
}
