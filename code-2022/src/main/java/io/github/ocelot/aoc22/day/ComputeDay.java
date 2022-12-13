package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.Loader;
import io.github.ocelot.aoc22.data.DayData;
import io.github.ocelot.aoc22.shader.ShaderBuffer;
import io.github.ocelot.aoc22.shader.ShaderProgram;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.lwjgl.opengl.GL42C.glMemoryBarrier;
import static org.lwjgl.opengl.GL43C.GL_SHADER_STORAGE_BARRIER_BIT;
import static org.lwjgl.opengl.GL43C.glDispatchCompute;

public abstract class ComputeDay<I extends DayData, O extends DayData> extends Day
{
    private final int outputSize;
    private final String shaderName;
    private CompletableFuture<ShaderProgram> shader;

    protected ComputeDay(int outputSize, String shaderName)
    {
        this.outputSize = outputSize;
        this.shaderName = shaderName;
    }

    protected abstract I processInput(String input) throws Exception;

    protected abstract void processOutput(O output) throws Exception;

    protected abstract O create(ByteBuffer buffer) throws Exception;

    @Override
    public void run(String input) throws Exception
    {
        I in = this.processInput(input);

        try (
                ShaderProgram shader = this.shader.join();
                ShaderBuffer inBuffer = new ShaderBuffer();
                ShaderBuffer outBuffer = new ShaderBuffer();
                MemoryStack stack = MemoryStack.stackPush()
        )
        {
            ByteBuffer data = stack.malloc(in.size());
            in.write(data);
            inBuffer.write(data);
            outBuffer.init(this.outputSize);

            shader.bind();
            inBuffer.bind(0);
            outBuffer.bind(1);
            glDispatchCompute(1, 1, 1);
            ShaderProgram.unbind();

            glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
            ByteBuffer out = stack.malloc(this.outputSize);
            outBuffer.read(out);
            this.processOutput(this.create(out));

            ShaderBuffer.unbind(0);
            ShaderBuffer.unbind(1);
        }
    }

    @Override
    public CompletableFuture<?> getDependencies(Executor mainExecutor, Executor backgroundExecutor)
    {
        if (this.shader == null)
        {
            return this.shader = Loader.loadShader("/days/" + this.shaderName + ".glsl", mainExecutor, backgroundExecutor);
        }
        return this.shader;
    }
}
