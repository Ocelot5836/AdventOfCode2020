package io.github.ocelot.passport;

import io.github.ocelot.IntRange;

public class IntRangePassportValidator implements PassportValidator
{
    private final IntRange range;

    public IntRangePassportValidator(int min, int max)
    {
        this.range = new IntRange(min, max);
    }

    @Override
    public boolean test(String s)
    {
        return this.range.test(Integer.parseInt(s));
    }
}
