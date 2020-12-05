package io.github.ocelot.passport;

import java.util.function.Predicate;

/**
 * <p>A single field that is on a passport.</p>
 *
 * @author Ocelot
 */
public class PassportField implements Predicate<String>
{
    private final String name;
    private final PassportValidator validator;

    public PassportField(String name, PassportValidator validator)
    {
        this.name = name;
        this.validator = validator;
    }

    @Override
    public boolean test(String s)
    {
        return this.validator.test(s);
    }

    /**
     * @return The key for the field to fetch
     */
    public String getName()
    {
        return name;
    }
}
