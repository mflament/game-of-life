package org.yah.games.gameoflife.counter;

import org.lwjgl.glfw.GLFW;

public class TimeTracker {

	private int count;

	private double processingTime;

	private long lastUpdate;

	public TimeTracker() {
		GLFW.glfwInit();
		lastUpdate = System.currentTimeMillis();
	}

	public void track(Runnable call) {
		double start = GLFW.glfwGetTime();
		call.run();
		processingTime += GLFW.glfwGetTime() - start;
		count++;
		if (System.currentTimeMillis() - lastUpdate >= 1000) {
			double ups = count / processingTime;
			System.out.println(String.format("%,d update/s", (int) ups));
			lastUpdate = System.currentTimeMillis();
		}
	}

}
