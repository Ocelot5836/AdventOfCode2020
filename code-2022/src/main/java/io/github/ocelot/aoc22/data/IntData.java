package io.github.ocelot.aoc22.data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public record IntData(int[] values) implements DayData
{
    public IntData(int size)
    {
        this(new int[size]);
    }

    @Override
    public String toString()
    {
        return Arrays.toString(this.values);
    }

    @Override
    public void read(ByteBuffer buffer)
    {
        buffer.asIntBuffer().get(this.values);
    }

    @Override
    public void write(ByteBuffer buffer)
    {
        buffer.asIntBuffer().put(this.values);
    }

    @Override
    public int size()
    {
        return this.values.length * Integer.BYTES;
    }
}
