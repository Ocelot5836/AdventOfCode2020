package io.github.ocelot.aoc22;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import io.github.ocelot.aoc22.day.Day;
import io.github.ocelot.aoc22.day.Day0;
import io.github.ocelot.window.Window;
import io.github.ocelot.window.WindowManager;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.NativeResource;
import org.slf4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import static org.lwjgl.glfw.GLFW.*;

public class Main extends TaskExecutor implements Runnable, NativeResource
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<Integer, Day> days;

    private final int day;
    private final ExecutorService backgroundLoader;
    private final WindowManager windowManager;
    private final Window window;

    private Thread mainThread;
    private volatile boolean running;

    public Main(int day)
    {
        super("Main");
        this.day = day;
        this.backgroundLoader = Executors.newCachedThreadPool();
        this.windowManager = new WindowManager();
        this.window = this.windowManager.create(800, 600, false);

        ImmutableMap.Builder<Integer, Day> builder = ImmutableMap.builder();
        builder.put(0, new Day0());
        this.days = builder.build();
    }

    public void start()
    {
        this.running = true;
    }

    @Override
    public void run()
    {
        this.mainThread = Thread.currentThread();
        this.running = true;

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        this.window.create("");
        GL.createCapabilities();

        GLUtil.setupDebugMessageCallback(new PrintStream(new OutputStream()
        {
            private StringBuffer buffer = new StringBuffer();

            @Override
            public void write(final int b)
            {
                if ((char) b == '\n')
                {
                    flush();
                    return;
                }
                this.buffer.append((char) b);
            }

            @Override
            public void flush()
            {
                LOGGER.debug(this.buffer.toString());
                this.buffer = new StringBuffer();
            }
        }));

        Day day = this.days.get(this.day);
        if (day == null)
        {
            throw new IllegalStateException("Unknown day: " + this.day);
        }

        CompletableFuture<String> input = Loader.loadInput("/data/" + this.day + ".txt", this.backgroundLoader);
        CompletableFuture<?> dependencies = day.getDependencies(this, this.backgroundLoader);

        CompletableFuture.allOf(input, dependencies)
                .thenRunAsync(() ->
                {
                    try
                    {
                        day.run(input.get());
                    }
                    catch (Exception e)
                    {
                        if (e instanceof CompletionException ce)
                        {
                            throw ce;
                        }
                        throw new CompletionException(e);
                    }
                    finally
                    {
                        LOGGER.info("Complete");
                        this.running = false;
                    }
                }, this)
                .handle((result, e) ->
                {
                    if (e != null)
                    {
                        LOGGER.error("Failed to run day " + this.day, e);
                        this.running = false;
                    }
                    return result;
                });

        try
        {
            while (this.running)
            {
                LockSupport.park(); // Wait until a task comes through, whether that be a tick or something else
                this.flushTasks();
            }
        }
        catch (Throwable t)
        {
            LOGGER.error("Fatal exception", t);
        }
    }

    @Override
    public void free()
    {
        this.windowManager.free();
        this.backgroundLoader.shutdown();
    }

    public static void main(String[] args)
    {
        int day = 0;
        if (args.length > 0)
        {
            day = Integer.parseInt(args[0]);
        }

        try (Main main = new Main(day))
        {
            Thread.currentThread().setName("Main");
            main.start();
            main.run();
        }
    }

    @Override
    public Thread getExecutorThread()
    {
        return mainThread;
    }
}
