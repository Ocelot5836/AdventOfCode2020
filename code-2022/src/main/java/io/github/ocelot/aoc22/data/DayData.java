package io.github.ocelot.aoc22.data;

import java.nio.ByteBuffer;

public interface DayData
{
    void write(ByteBuffer buffer);

    int size();
}
