package io.github.ocelot.passport;

import java.util.regex.Pattern;

public class RegexPassportValidator implements PassportValidator
{
    private final Pattern pattern;

    public RegexPassportValidator(String regex)
    {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean test(String s)
    {
        return this.pattern.matcher(s).matches();
    }
}
