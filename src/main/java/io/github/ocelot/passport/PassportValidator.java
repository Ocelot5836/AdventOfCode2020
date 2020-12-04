package io.github.ocelot.passport;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface PassportValidator extends Predicate<String>
{
    PassportValidator ALWAYS_TRUE = s -> true;

    static PassportValidator and(PassportValidator... validators)
    {
        return s ->
        {
            for (PassportValidator validator : validators)
                if (!validator.test(s))
                    return false;
            return true;
        };
    }

    static PassportValidator or(PassportValidator... validators)
    {
        return s ->
        {
            for (PassportValidator validator : validators)
                if (validator.test(s))
                    return true;
            return false;
        };
    }

    static PassportValidator range(int min, int max)
    {
        return s ->
        {
            int value = Integer.parseInt(s);
            return value >= min && value <= max;
        };
    }

    static PassportValidator length(int length)
    {
        return s -> s.length() == length;
    }

    static PassportValidator measurement(int min, int max, String suffix)
    {
        return s ->
        {
            if (!s.endsWith(suffix))
                return false;
            int number = Integer.parseInt(s.substring(0, s.length() - suffix.length()));
            return number >= min && number <= max;
        };
    }

    static PassportValidator regex(String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        return s -> pattern.matcher(s).matches();
    }
}
