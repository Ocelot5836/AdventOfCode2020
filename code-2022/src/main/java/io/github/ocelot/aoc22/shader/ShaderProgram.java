package io.github.ocelot.aoc22.shader;

import com.mojang.logging.LogUtils;
import org.lwjgl.system.NativeResource;
import org.slf4j.Logger;

import static org.lwjgl.opengl.GL20C.*;

public class ShaderProgram implements NativeResource
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private final int programId;

    public ShaderProgram(int programId)
    {
        this.programId = programId;
    }

    public void bind()
    {
        glUseProgram(this.programId);
    }

    public static void unbind()
    {
        glUseProgram(0);
    }

    public int getUniformLocation(CharSequence name)
    {
        int location = glGetUniformLocation(this.programId, name);
        if (location == -1)
        {
            LOGGER.error("Unknown uniform: " + name);
        }
        return location;
    }

    public void setInt(int location, int value)
    {
        glUniform1i(location, value);
    }

    @Override
    public void free()
    {
        glDeleteShader(this.programId);
    }
}
