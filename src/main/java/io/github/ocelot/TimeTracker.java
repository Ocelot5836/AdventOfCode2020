package io.github.ocelot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * <p>Tracks how long certain durations take.</p>
 *
 * @author Ocelot
 */
public class TimeTracker
{
    private static final String[] UNITS = {"ns", "micros", "ms", "s", "min", "h", "d"};
    private final List<Section> sections;
    private long totalDuration;
    private boolean tracking;

    public TimeTracker()
    {
        this.sections = new ArrayList<>();
    }

    /**
     * Starts tracking a new section.
     *
     * @param name The name of the new section
     */
    public void startSection(String name)
    {
        if (this.tracking)
            throw new IllegalStateException("Cannot start tracking a new section if already tracking");
        this.tracking = true;
        this.sections.add(new Section(name));
    }

    /**
     * Ends the current section and begins a new one.
     * @param name The name of the new section
     */
    public void endStartSection(String name)
    {
        this.endSection();
        this.startSection(name);
    }

    /**
     * Ends the tracking for the current section.
     */
    public void endSection()
    {
        if (!this.tracking)
            throw new IllegalStateException("Cannot end section before starting");
        this.tracking = false;
        Section section = this.sections.get(this.sections.size() - 1);
        section.endSection();
        this.totalDuration += section.endTime - section.startTime;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        this.sections.forEach(section -> section.addSection(s -> builder.append(s).append(", ")));
        builder.append("overall took ").append(abbreviate(this.totalDuration));
        return "Time Tracker: " + builder.toString();
    }

    private static class Section
    {
        private final String name;
        private final long startTime;
        private long endTime;

        private Section(String name)
        {
            this.name = name;
            this.startTime = System.nanoTime();
        }

        private void endSection()
        {
            if (this.endTime != 0)
                throw new IllegalStateException("Cannot end section twice");
            this.endTime = System.nanoTime();
        }

        private void addSection(Consumer<String> consumer)
        {
            if (this.endTime == 0)
            {
                consumer.accept(this.name + " still processing");
                return;
            }
            consumer.accept(this.name + " took " + abbreviate(this.endTime - this.startTime));
        }
    }

    private static String abbreviate(long nanos)
    {
        TimeUnit unit = abbreviateUnit(nanos);
        return unit.convert(nanos, TimeUnit.NANOSECONDS) + UNITS[unit.ordinal()];
    }

    private static TimeUnit abbreviateUnit(long nanos)
    {
        for (int i = 0; i < TimeUnit.values().length; i++)
        {
            TimeUnit unit = TimeUnit.values()[TimeUnit.values().length - i - 1];
            if (unit.convert(nanos, NANOSECONDS) > 0)
                return unit;
        }
        return NANOSECONDS;
    }
}
