package io.github.ocelot.aoc22.shader;

import org.lwjgl.system.NativeResource;

import static org.lwjgl.opengl.GL20C.glDeleteShader;
import static org.lwjgl.opengl.GL20C.glUseProgram;

public class ShaderProgram implements NativeResource
{
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

    @Override
    public void free()
    {
        glDeleteShader(this.programId);
    }
}
