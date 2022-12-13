package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.Loader;
import io.github.ocelot.aoc22.data.DayData;
import io.github.ocelot.aoc22.shader.ShaderBuffer;
import io.github.ocelot.aoc22.shader.ShaderError;
import io.github.ocelot.aoc22.shader.ShaderProgram;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL42C.glMemoryBarrier;
import static org.lwjgl.opengl.GL43C.GL_SHADER_STORAGE_BARRIER_BIT;
import static org.lwjgl.opengl.GL43C.glDispatchCompute;

public abstract class ComputeDay<I extends DayData, O extends DayData> extends Day
{
    private final O output;
    private final String shaderName;
    private CompletableFuture<ShaderProgram> shader;

    protected ComputeDay(O output, String shaderName)
    {
        this.output = output;
        this.shaderName = shaderName;
    }

    protected abstract I processInput(String input) throws Exception;

    protected void processOutput(O output) throws Exception
    {
        this.logger.info("Got " + output);
    }

    @Override
    public void run(String input, int part) throws Exception
    {
        I in = this.processInput(input);
        this.logger.info("Input " + in);

        try (
                ShaderProgram shader = this.shader.join();
                ShaderBuffer inBuffer = new ShaderBuffer();
                ShaderBuffer outBuffer = new ShaderBuffer();
                MemoryStack stack = MemoryStack.stackPush()
        )
        {
            ByteBuffer data = stack.malloc(Integer.BYTES + in.size());
            data.asIntBuffer().put(part); // Add part first
            data.position(Integer.BYTES);
            in.write(data);
            data.rewind();
            inBuffer.write(data);

            outBuffer.init(Integer.BYTES + this.output.size());

            shader.bind();
            inBuffer.bind(0);
            outBuffer.bind(1);
            glDispatchCompute(1, 1, 1);
            ShaderProgram.unbind();

            glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
            ByteBuffer out = stack.malloc(Integer.BYTES + this.output.size());
            outBuffer.read(out);

            int error = out.getInt();
            if (error != 0)
            {
                throw new IllegalStateException(ShaderError.getErrorString(error));
            }

            this.output.read(out);
            this.processOutput(this.output);

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
