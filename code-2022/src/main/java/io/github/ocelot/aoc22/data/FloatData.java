package io.github.ocelot.aoc22.data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public record FloatData(float... values) implements DayData
{
    public FloatData(int size, ByteBuffer buffer)
    {
        this(getFloats(size, buffer));
    }

    @Override
    public String toString()
    {
        return Arrays.toString(this.values);
    }

    private static float[] getFloats(int size, ByteBuffer buffer)
    {
        float[] output = new float[size];
        buffer.asFloatBuffer().get(output);
        return output;
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
