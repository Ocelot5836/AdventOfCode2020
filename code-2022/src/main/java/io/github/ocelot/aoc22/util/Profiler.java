package io.github.ocelot.aoc22.util;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class Profiler
{
    private final List<Pair<String, Stopwatch>> times;
    private String currentName;
    private Stopwatch currentTime;

    public Profiler()
    {
        this.times = new LinkedList<>();
    }

    public void flush()
    {
        if (this.currentName != null && this.currentTime.isRunning())
        {
            this.pop();
        }
    }

    public void push(String name)
    {
        this.flush();
        this.currentName = name;
        this.currentTime = Stopwatch.createStarted();
    }

    public void pop()
    {
        this.currentTime.stop();
        this.times.add(Pair.of(this.currentName, this.currentTime));
    }

    public void print(Logger logger)
    {
        this.times.forEach(pair -> logger.info(pair.left() + " took " + pair.right()));
    }
}
