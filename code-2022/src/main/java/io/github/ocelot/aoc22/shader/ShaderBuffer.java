package io.github.ocelot.aoc22.shader;

import org.lwjgl.system.NativeResource;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL30C.glBindBufferBase;
import static org.lwjgl.opengl.GL43C.GL_SHADER_STORAGE_BUFFER;

public class ShaderBuffer implements NativeResource
{
    private final int id;

    public ShaderBuffer()
    {
        this.id = glGenBuffers();
    }

    public void bind(int index)
    {
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, index, this.id);
    }

    public static void unbind(int index)
    {
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, index, 0);
    }

    public void write(ByteBuffer data)
    {
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, this.id);
        glBufferData(GL_SHADER_STORAGE_BUFFER, data, GL_STATIC_DRAW);
    }

    public void init(long size)
    {
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, this.id);
        glBufferData(GL_SHADER_STORAGE_BUFFER, size, GL_STATIC_READ);
    }

    public void read(ByteBuffer buffer)
    {
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, this.id);
        glGetBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, buffer);
    }

    @Override
    public void free()
    {
        glDeleteBuffers(this.id);
    }
}
