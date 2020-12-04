package io.github.ocelot.passport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetPassportValidator implements PassportValidator
{
    private final Set<String> values;

    public SetPassportValidator(String... values)
    {
        this.values = new HashSet<>(Arrays.asList(values));
    }

    @Override
    public boolean test(String s)
    {
        return this.values.contains(s);
    }
}
