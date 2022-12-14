package io.github.ocelot.aoc22.util;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.LockSupport;

public abstract class TaskExecutor implements Executor {

   private static final Logger LOGGER = LogUtils.getLogger();
   private final String name;
   private final Queue<Runnable> pendingTasks;

   public TaskExecutor(String name) {
      this.name = name;
      this.pendingTasks = new ConcurrentLinkedDeque<>();
   }

   /**
    * @return The thread this executor operates on
    */
   public abstract Thread getExecutorThread();

   /**
    * @return If tasks are being queued instead of immediately run
    */
   public boolean shouldScheduleTasks() {
      return Thread.currentThread() != this.getExecutorThread();
   }

   @Override
   public void execute(@NotNull Runnable command) {
      if (this.shouldScheduleTasks()) {
         this.defer(command);
      } else {
         command.run();
      }
   }

   /**
    * Waits until the next execution period to run the specified task.
    * @param command The command to execute
    */
   public void defer(@Nullable Runnable command) {
      this.pendingTasks.add(command);
      LockSupport.unpark(this.getExecutorThread());
   }

   /**
    * Flushes all tasks queued.
    */
   public void flushTasks() {
      while(!this.pendingTasks.isEmpty()) {
         if (!this.pollTask())
            break;
      }
   }

   /**
    * @return If there are more tasks to poll
    */
   public boolean pollTask() {
      Runnable runnable = this.pendingTasks.peek();
      if (runnable == null)
         return false;

      try {
         this.pendingTasks.remove().run();
      } catch (Exception e) {
         LOGGER.error(LogUtils.FATAL_MARKER, "Error executing task on {}", this.name, e);
      }
      return true;
   }
}
