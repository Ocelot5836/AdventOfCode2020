package io.github.ocelot.aoc22.data;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

public class StringData implements DayData
{
    private String value;

    public StringData(int expectedLength)
    {
        this(new String(new byte[expectedLength]));
    }

    public StringData(String value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringData that = (StringData) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value);
    }

    @Override
    public String toString()
    {
        return this.value;
    }

    @Override
    public void read(ByteBuffer buffer)
    {
        IntBuffer intBuffer = buffer.asIntBuffer();
        char[] chars = new char[intBuffer.get()];
        for (int i = 0; i < chars.length; i++)
        {
            chars[i] = (char) intBuffer.get();
        }
        this.value = new String(chars);
    }

    @Override
    public void write(ByteBuffer buffer)
    {
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(this.value.length());
        intBuffer.put(this.value.chars().toArray());
    }

    @Override
    public int size()
    {
        return (1 + this.value.length()) * Integer.BYTES;
    }
}
