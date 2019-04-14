package org.yah.games.opengl;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;

public abstract class GLApplication {

	private long window;

	private Color4f clearColor;

	protected int viewWidth;

	protected int viewHeight;

	public void start() {
		setup();

		try {
			loop();
		} finally {
			destroy();
		}
	}

	private void setup() {
		if (isDebugEnabled()) {
		}

		this.viewWidth = initialViewWidth();
		this.viewHeight = initialViewHeight();

		window = glfwCreateWindow(viewWidth, viewHeight, getTitle(), NULL, NULL);
		if (window == NULL) {
			throw new IllegalStateException("Unable to create window");
		}

		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
		glfwSetKeyCallback(window, this::keyEvent);

		centerWindow();

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		
		// Enable v-sync
		glfwSwapInterval(isVSyncEnabled() ? 1 : 0);

		// Make the window visible
		glfwShowWindow(window);

		clearColor = getClearColor();

		GL.createCapabilities();

		glfwSetFramebufferSizeCallback(window, (window, width, height) -> frameBufferResized(width, height));

		if (isDebugEnabled()) {
			GLUtil.setupDebugMessageCallback();
		}

		loadResources();

		checkError("setup");
	}

	protected final void checkError(String operation) {
		int error = glGetError();
		if (error != GL_NO_ERROR) {
			throw new GLException("Error during " + operation + " : " + error);
		}
	}

	protected int initialViewWidth() {
		return 512;
	}

	protected int initialViewHeight() {
		return 512;
	}

	protected Color4f getClearColor() {
		return new Color4f(0.0f, 0.0f, 0.0f, 0.0f);
	}

	private void loop() {
		GL.createCapabilities();
		glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			render();
			glfwSwapBuffers(window); // swap the color buffers
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	private void destroy() {
		cleanup();

		// Free the window callbacks and destroy the window
		glfwSetKeyCallback(window, null).free();
		glfwSetFramebufferSizeCallback(window, null).free();

		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void centerWindow() {
		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically
	}

	private void keyEvent(long window, int key, int scancode, int action, int mods) {
		switch (action) {
		case GLFW_PRESS:
			keyPressed(key, scancode, mods);
			break;
		case GLFW_RELEASE:
			keyReleased(key, scancode, mods);
			break;
		case GLFW_REPEAT:
			keyRepeated(key, scancode, mods);
			break;
		default:
			break;
		}
	}

	protected abstract void render();

	protected void frameBufferResized(int width, int height) {
		this.viewWidth = width;
		this.viewHeight = height;
		glViewport(0, 0, viewWidth, viewHeight);
	}

	protected void keyPressed(int key, int scancode, int mods) {}

	protected void keyReleased(int key, int scancode, int mods) {
		switch (key) {
		case GLFW_KEY_ESCAPE:
			glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			break;
		default:
			break;
		}
	}

	protected void keyRepeated(int key, int scancode, int mods) {}

	protected void loadResources() {}

	protected void cleanup() {}

	protected abstract String getTitle();

	protected boolean isVSyncEnabled() {
		return true;
	}

	protected boolean isDebugEnabled() {
		return false;
	}

}
