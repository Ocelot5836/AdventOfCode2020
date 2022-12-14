package io.github.ocelot.aoc22.day;

import io.github.ocelot.aoc22.util.Loader;
import io.github.ocelot.aoc22.util.Profiler;
import io.github.ocelot.aoc22.data.DayData;
import io.github.ocelot.aoc22.data.StringData;
import io.github.ocelot.aoc22.shader.ShaderBuffer;
import io.github.ocelot.aoc22.shader.ShaderProgram;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.lwjgl.opengl.GL20C.glUniform1i;
import static org.lwjgl.opengl.GL42C.glMemoryBarrier;
import static org.lwjgl.opengl.GL43C.GL_SHADER_STORAGE_BARRIER_BIT;
import static org.lwjgl.opengl.GL43C.glDispatchCompute;

public abstract class ComputeDay<I extends DayData> extends Day
{
    private final DayData output;
    private final String shaderName;
    private CompletableFuture<ShaderProgram> shader;

    protected ComputeDay(DayData output, String shaderName)
    {
        this.output = output;
        this.shaderName = shaderName;
    }

    protected abstract I processInput(String input) throws Exception;

    @Override
    public void run(String input, Profiler profiler) throws Exception
    {
        I in = this.processInput(input);
        this.logger.info("Input " + in);

        try (
                ShaderProgram shader = this.shader.join();
                ShaderBuffer errorBuffer = new ShaderBuffer();
                ShaderBuffer inBuffer = new ShaderBuffer();
                ShaderBuffer outBuffer = new ShaderBuffer();
                MemoryStack stack = MemoryStack.stackPush()
        )
        {
            ByteBuffer data = stack.malloc(in.size());
            in.write(data);
            inBuffer.write(data);

            errorBuffer.init(StringData.MAX_SIZE * Integer.BYTES);
            outBuffer.init(this.output.size());

            shader.bind();
            errorBuffer.bind(0);
            inBuffer.bind(1);
            outBuffer.bind(2);

            ByteBuffer error = stack.malloc(StringData.MAX_SIZE * Integer.BYTES);
            ByteBuffer out = stack.malloc(this.output.size());
            StringData errorData = new StringData();
            int partLocation = shader.getUniformLocation("in_Part");
            for (int part = 0; part < 2; part++)
            {
                profiler.push("Part " + (part + 1));
                glUniform1i(partLocation, part);
                glDispatchCompute(1, 1, 1);
                glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT); // Wait for the data to be output

                errorBuffer.read(error);
                errorData.read(error);
                if (!errorData.toString().isBlank())
                {
                    throw new IllegalStateException("Shader Error: " + errorData);
                }

                outBuffer.read(out);
                this.output.read(out);
                this.logger.info("Result of part " + (part + 1) + " " + this.output);
                profiler.pop();
            }

            ShaderBuffer.unbind(0);
            ShaderBuffer.unbind(1);
            ShaderBuffer.unbind(2);
            ShaderProgram.unbind();
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
