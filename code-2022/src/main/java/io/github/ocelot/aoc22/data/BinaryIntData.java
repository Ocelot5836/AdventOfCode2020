package io.github.ocelot.aoc22.data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public record BinaryIntData(int[] values) implements DayData
{
    public BinaryIntData(int size)
    {
        this(new int[size]);
    }

    @Override
    public String toString()
    {
        int iMax = this.values.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0; ; i++) {
            builder.append(Integer.toBinaryString(this.values[i]));
            if (i >= iMax)
                return builder.append(']').toString();
            builder.append(", ");
        }
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
