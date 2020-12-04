package io.github.ocelot.passport;

public class AndPassportValidator implements PassportValidator
{
    private final PassportValidator[] validators;

    public AndPassportValidator(PassportValidator... validators)
    {
        this.validators = validators;
    }

    @Override
    public boolean test(String s)
    {
        for (PassportValidator validator : this.validators)
            if (!validator.test(s))
                return false;
        return true;
    }
}
