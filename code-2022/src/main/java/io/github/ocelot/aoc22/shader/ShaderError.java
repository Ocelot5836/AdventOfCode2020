package io.github.ocelot.aoc22.shader;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public enum ShaderError
{
    ;

    public static final String CODE;

    static
    {
        StringBuffer code = new StringBuffer();
        for (ShaderError error : values())
        {
            code.append("#define ").append(error.name()).append(" ").append(1 << error.ordinal()).append('\n');
        }
        CODE = code.toString();
    }

    private final String description;

    ShaderError()
    {
        this(null);
    }

    ShaderError(@Nullable String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return this.description != null ? this.name() + ": " + this.description : this.name();
    }

    public static String getErrorString(int code)
    {
        List<String> errors = new ArrayList<>(1);
        for (ShaderError error : values())
        {
            if ((code >> error.ordinal() & 1) > 0)
            {
                errors.add(error.toString());
            }
        }
        return code + ": " + String.join(", ", errors);
    }
}
