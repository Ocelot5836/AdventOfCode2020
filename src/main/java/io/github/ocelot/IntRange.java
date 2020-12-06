package io.github.ocelot;

import java.util.function.Predicate;

/**
 * Denotes a range of integer values.
 *
 * @author Ocelot
 */
public class IntRange implements Predicate<Double>
{
    private final int min;
    private final int max;

    public IntRange(int min, int max)
    {
        if (min > max)
            throw new IllegalArgumentException("Min (" + min + ") must be less than or equal to max (" + max + ")");
        this.min = min;
        this.max = max;
    }

    /**
     * Parses an int range from a string sequence of <code>#-#</code>
     *
     * @param input The input string to parse
     * @return A new integer range from the input data
     */
    public static IntRange parse(String input)
    {
        String[] parts = input.split("-");
        if (parts.length != 2)
            throw new IllegalArgumentException("Range must be two parts");
        return new IntRange(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    }

    @Override
    public boolean test(Double number)
    {
        return number >= this.min && number <= this.max;
    }

    /**
     * @return The minimum value a number in this range can be
     */
    public int getMin()
    {
        return min;
    }

    /**
     * @return The maximum value a number in this range can be
     */
    public int getMax()
    {
        return max;
    }

    /**
     * @return The center value of this range
     */
    public double getMedian()
    {
        return this.min + (this.max - this.min) / 2.0;
    }

    @Override
    public String toString()
    {
        return "IntRange{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
