package io.github.ocelot.aoc22.day;

import com.mojang.logging.LogUtils;
import io.github.ocelot.aoc22.util.Profiler;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class Day
{
    protected final Logger logger;

    protected Day()
    {
        this.logger = LogUtils.getLogger();
    }

    public abstract void run(String input, Profiler profiler) throws Exception;

    public CompletableFuture<?> getDependencies(Executor mainExecutor, Executor backgroundExecutor)
    {
        return CompletableFuture.completedFuture(null);
    }
}
