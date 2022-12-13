package io.github.ocelot.aoc22;

import com.mojang.logging.LogUtils;
import io.github.ocelot.aoc22.shader.ShaderError;
import io.github.ocelot.aoc22.shader.ShaderProcessor;
import io.github.ocelot.aoc22.shader.ShaderProgram;
import org.slf4j.Logger;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL43C.GL_COMPUTE_SHADER;

public final class Loader
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Lock DEPENDENCY_LOCK = new ReentrantLock();
    private static final Map<String, CompletableFuture<String>> DEPENDENCIES = new ConcurrentHashMap<>();

    public static CompletableFuture<ShaderProgram> loadShader(String name, Executor mainExecutor, Executor backgroundExecutor)
    {
        return loadInput(name, backgroundExecutor).thenComposeAsync(data ->
        {
            Set<CompletableFuture<?>> dependencies = new HashSet<>();

            String[] lines = data.split("\n");
            for (String line : lines)
            {
                if (!line.startsWith("#include"))
                {
                    continue;
                }

                String dependency = line.substring("#include".length()).trim();
                if (!DEPENDENCIES.containsKey(dependency))
                {
                    try
                    {
                        DEPENDENCY_LOCK.lock();
                        if (!DEPENDENCIES.containsKey(dependency))
                        {
                            DEPENDENCIES.put(dependency, loadInput("/include/" + dependency, backgroundExecutor).thenApplyAsync(dependencyData ->
                            {
                                LOGGER.info("Loaded dependency: " + dependency);
                                return dependencyData;
                            }, mainExecutor));
                        }
                    }
                    finally
                    {
                        DEPENDENCY_LOCK.unlock();
                    }
                }

                dependencies.add(DEPENDENCIES.get(dependency));
            }
            return CompletableFuture.allOf(dependencies.toArray(CompletableFuture[]::new)).thenApply(__ -> data);
        }, backgroundExecutor).thenApplyAsync(data ->
        {
            String[] lines = data.split("\n");
            for (int i = 0; i < lines.length; i++)
            {
                String line = lines[i];
                if (!line.startsWith("#include"))
                {
                    continue;
                }

                String dependency = line.substring("#include".length()).trim();
                lines[i] = DEPENDENCIES.get(dependency).join();
            }

            return String.join("\n", ShaderProcessor.processSource(lines));
        }, backgroundExecutor).thenApplyAsync(data ->
        {
            int programId = glCreateProgram();
            ShaderProgram program = new ShaderProgram(programId);
            int shader = glCreateShader(GL_COMPUTE_SHADER);

            try
            {

                glShaderSource(shader, data);
                glCompileShader(shader);

                if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
                {
                    String log = glGetShaderInfoLog(shader, 512);
                    LOGGER.error(log);
                    throw new IllegalStateException("Failed to compile shader");
                }

                glAttachShader(programId, shader);
                glLinkProgram(programId);

                if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE)
                {
                    String log = glGetProgramInfoLog(programId, 512);
                    LOGGER.error(log);
                    throw new IllegalStateException("Failed to link shader");
                }

                return program;
            }
            catch (Exception e)
            {
                program.free();
                throw new CompletionException(e);
            }
            finally
            {
                glDeleteShader(shader);
            }
        }, mainExecutor);
    }

    public static CompletableFuture<String> loadInput(String name, Executor backgroundExecutor)
    {
        if ("/include/errors.glsl".equals(name))
            return CompletableFuture.completedFuture(ShaderError.CODE);
        return CompletableFuture.supplyAsync(() ->
        {
            try
            {
                InputStream stream = Loader.class.getResourceAsStream(name);
                if (stream == null)
                {
                    throw new FileNotFoundException(name);
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream)))
                {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            }
            catch (IOException e)
            {
                throw new CompletionException(e);
            }
        }, backgroundExecutor);
    }
}
