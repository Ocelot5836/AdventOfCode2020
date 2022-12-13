package io.github.ocelot.aoc22.data;

import java.nio.ByteBuffer;

public interface DayData
{
    void read(ByteBuffer buffer);

    void write(ByteBuffer buffer);

    int size();
}
