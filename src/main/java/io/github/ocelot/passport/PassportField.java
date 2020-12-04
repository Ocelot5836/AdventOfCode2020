package io.github.ocelot.passport;

import java.util.function.Predicate;

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

    public String getName()
    {
        return name;
    }
}
