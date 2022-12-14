package io.github.ocelot.aoc22.data;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

public class StringData implements DayData
{
    public static final int MAX_SIZE = 128;

    private String value;

    public StringData()
    {
        this(new String(new byte[MAX_SIZE]));
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
        return this.value.replaceAll("\n", "\\\\n");
    }

    @Override
    public void read(ByteBuffer buffer)
    {
        IntBuffer intBuffer = buffer.asIntBuffer();
        char[] chars = new char[MAX_SIZE];
        int length = 0;
        for (int i = 0; i < chars.length; i++)
        {
            chars[i] = (char) intBuffer.get();
            if (chars[i] == 0)
            {
                break;
            }
            length++;
        }
        this.value = new String(chars, 0, length);
    }

    @Override
    public void write(ByteBuffer buffer)
    {
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(this.value.chars().toArray());
        intBuffer.put(0);
    }

    @Override
    public int size()
    {
        return (this.value.length() + 1) * Integer.BYTES;
    }
}
