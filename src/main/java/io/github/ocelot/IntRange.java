package io.github.ocelot;

import java.util.function.Predicate;

public class IntRange implements Predicate<Integer>
{
    private final int min;
    private final int max;

    public IntRange(int min, int max)
    {
        if (min > max)
            throw new IllegalArgumentException("Min must be less than or equal to max");
        this.min = min;
        this.max = max;
    }

    public static IntRange parse(String input)
    {
        String[] parts = input.split("-");
        if (parts.length != 2)
            throw new IllegalArgumentException("Range must be two parts");
        return new IntRange(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    }

    @Override
    public boolean test(Integer integer)
    {
        return integer >= this.min && integer <= this.max;
    }
}
