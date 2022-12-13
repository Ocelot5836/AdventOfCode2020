package io.github.ocelot.passport;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * <p>Validates a passport field.</p>
 *
 * @author Ocelot
 */
public interface PassportValidator extends Predicate<String>
{
    PassportValidator ALWAYS_TRUE = s -> true;

    /**
     * Creates a new validator for a compound and operation.
     *
     * @param validators The child validators to chain together
     * @return A validator that returns <code>true</code> when all children are also true
     */
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

    /**
     * Creates a new validator for a compound or operation.
     *
     * @param validators The child validators to chain together
     * @return A validator that returns <code>true</code> when any one child is true
     */
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

    /**
     * Creates a new validator for a number range.
     *
     * @param min The minimum value the number can be
     * @param max The maximum value the number can be
     * @return A validator that returns <code>true</code> when the value is a number between the min and max
     */
    static PassportValidator range(int min, int max)
    {
        return s ->
        {
            int value = Integer.parseInt(s);
            return value >= min && value <= max;
        };
    }

    /**
     * Creates a new validator for a string length.
     *
     * @param length The length the string must be
     * @return A validator that returns <code>true</code> when the string has a length that equals the length
     */
    static PassportValidator length(int length)
    {
        return s -> s.length() == length;
    }

    /**
     * Creates a new validator for a type of ranged measurement.
     *
     * @param min  The minimum value the measurement can be
     * @param max  The maximum value the measurement can be
     * @param unit The unit for the type of measurement
     * @return A validator that returns <code>true</code> when the value is a number between the min and max in the specified units
     */
    static PassportValidator measurement(int min, int max, String unit)
    {
        return s ->
        {
            if (!s.endsWith(unit))
                return false;
            int number = Integer.parseInt(s.substring(0, s.length() - unit.length()));
            return number >= min && number <= max;
        };
    }

    /**
     * Creates a new validator for a regex pattern.
     *
     * @param regex The pattern to use
     * @return A validator that returns <code>true</code> when the pattern matches the value
     */
    static PassportValidator regex(String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        return s -> pattern.matcher(s).matches();
    }
}
