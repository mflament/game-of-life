package org.yah.games.gameoflife;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.lwjgl.glfw.GLFW;

/**
 * All time are in seconds
 */
public class ExecutionTimeTracker {

	private int executionCount;

	private double totalTime;

	private double minTime = Double.MAX_VALUE;

	private double maxTime = Double.MIN_VALUE;

	public ExecutionTimeTracker() {
		GLFW.glfwInit();
	}

	public void track(Runnable action) {
		track(() -> {
			action.run();
			return null;
		});
	}

	public <T> T track(Supplier<T> action) {
		double start = glfwGetTime();
		T res = action.get();
		double elapsed = glfwGetTime() - start;
		totalTime += elapsed;
		minTime = Math.min(elapsed, minTime);
		maxTime = Math.max(elapsed, maxTime);
		executionCount++;
		return res;
	}

	public ExecutionTimeSnapshot snapshot() {
		return snapshot(true);
	}

	public ExecutionTimeSnapshot snapshot(boolean reset) {
		ExecutionTimeSnapshot snapshot = new ExecutionTimeSnapshot(this);
		if (reset) {
			executionCount = 0;
			totalTime = 0;
			minTime = Double.MAX_VALUE;
			maxTime = Double.MIN_VALUE;
		}
		return snapshot;
	}

	public static class ExecutionTimeSnapshot {
		private final int executionCount;

		private final double totalTime;
		private final double minTime;
		private final double maxTime;

		private ExecutionTimeSnapshot(ExecutionTimeTracker timeTracker) {
			this.executionCount = timeTracker.executionCount;
			this.totalTime = timeTracker.totalTime;
			this.minTime = timeTracker.minTime;
			this.maxTime = timeTracker.maxTime;
		}

		public int getExecutionCount() {
			return executionCount;
		}

		public double getTotalTime(TimeUnit timeUnit) {
			long factor = timeUnit.convert(1, TimeUnit.SECONDS);
			return totalTime * factor;
		}

		public double getAvgExecutionTime(TimeUnit timeUnit) {
			if (executionCount == 0)
				return 0;
			long factor = timeUnit.convert(1, TimeUnit.SECONDS);
			return (totalTime * factor) / executionCount;
		}

		public double getExecutionPer(TimeUnit timeUnit) {
			if (totalTime == 0)
				return 0;

			long factor = timeUnit.convert(1, TimeUnit.SECONDS);
			return executionCount / (totalTime * factor);
		}

		public double getMinTime(TimeUnit timeUnit) {
			long factor = timeUnit.convert(1, TimeUnit.SECONDS);
			return minTime * factor;
		}

		public double getMaxTime(TimeUnit timeUnit) {
			long factor = timeUnit.convert(1, TimeUnit.SECONDS);
			return maxTime * factor;
		}

		@Override
		public String toString() {
			if (executionCount == 0)
				return "No execution";

			return String.format("%d executions in %dms [%de/s] [%.2f < %.2f < %.2f ms/e]",
					executionCount, // number of executions
					(long) getTotalTime(TimeUnit.MILLISECONDS), // total execution time
					(long) getExecutionPer(TimeUnit.SECONDS), // execution / s
					getMinTime(TimeUnit.MILLISECONDS), getAvgExecutionTime(TimeUnit.MILLISECONDS), getMaxTime(
							TimeUnit.MILLISECONDS));

		}
	}
}
