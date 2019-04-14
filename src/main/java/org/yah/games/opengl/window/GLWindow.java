package org.yah.games.opengl.window;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DEPTH_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_STENCIL_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;
import org.yah.games.opengl.GLException;

public final class GLWindow {

	private final long window;

	protected final int width;

	protected final int height;

	private final KeyHandler keyPressedHandler;

	private final KeyHandler keyReleasedHandler;

	private final KeyHandler keyRepeatHandler;

	private GLWindow(Builder builder) {
		this.window = builder.window;
		this.width = builder.width;
		this.height = builder.height;
		this.keyPressedHandler = new CloseKeyHandler(builder.keyPressHandler);
		this.keyReleasedHandler = builder.keyReleaseHandler;
		this.keyRepeatHandler = builder.keyRepeatHandler;
		glfwSetKeyCallback(window, this::keyEvent);
	}

	public void show() {
		glfwShowWindow(window);
	}

	public void centerOnScreen() {
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

	public void requestClose() {
		glfwSetWindowShouldClose(window, true);
	}

	public boolean isCloseRequested() {
		return glfwWindowShouldClose(window);
	}

	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}

	public void swapBuffers() {
		glfwSwapBuffers(window);
	}

	public void makeContextCurrent() {
		glfwMakeContextCurrent(window);
	}

	private void keyEvent(long window, int key, int scancode, int action, int mods) {
		KeyHandler handler;
		switch (action) {
		case GLFW_PRESS:
			handler = keyPressedHandler;
			break;
		case GLFW_RELEASE:
			handler = keyReleasedHandler;
			break;
		case GLFW_REPEAT:
			handler = keyRepeatHandler;
			break;
		default:
			return;
		}
		handler.handle(key, scancode, mods);
	}

	public class CloseKeyHandler implements KeyHandler {
		private final KeyHandler delegate;

		public CloseKeyHandler(KeyHandler delegate) {
			super();
			this.delegate = delegate;
		}

		@Override
		public void handle(int key, int scancode, int mods) {
			if (key == GLFW_KEY_ESCAPE)
				requestClose();
			else
				delegate.handle(key, scancode, mods);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public interface KeyHandler {
		void handle(int key, int scancode, int mods);
	}

	public static final class Builder {

		private static KeyHandler NO_OP_KEYHANDLER = (key, scancode, mods) -> {};

		private long window;

		protected int width = 512;
		protected int height = 512;

		private boolean debug = Configuration.DEBUG.get(false);

		private String title = "GL Application";

		private boolean vsync;

		private KeyHandler keyPressHandler = NO_OP_KEYHANDLER;

		private KeyHandler keyReleaseHandler = NO_OP_KEYHANDLER;

		private KeyHandler keyRepeatHandler = NO_OP_KEYHANDLER;

		private Builder() {
			// Setup an error callback.
			GLFWErrorCallback.createThrow().set();

			// Initialize GLFW. Most GLFW functions will not work before doing this.
			if (!glfwInit())
				throw new IllegalStateException("Unable to initialize GLFW");

			// Configure GLFW
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
			glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

			glfwWindowHint(GLFW_DEPTH_BITS, 24);
			glfwWindowHint(GLFW_STENCIL_BITS, 8);
		}

		public Builder withWidth(int width) {
			this.width = width;
			return this;
		}

		public Builder withHeight(int height) {
			this.height = height;
			return this;
		}

		public Builder withMajorVersion(int version) {
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, version);
			return this;
		}

		public Builder withMinorVersion(int version) {
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, version);
			return this;
		}

		public Builder withCoreProfile() {
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
			return this;
		}

		public Builder withDebug() {
			glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
			debug = true;
			return this;
		}

		public Builder withKeyPressHandler(KeyHandler handler) {
			this.keyPressHandler = handler;
			return this;
		}

		public Builder withKeyRealseHandler(KeyHandler handler) {
			this.keyReleaseHandler = handler;
			return this;
		}

		public Builder withKeyRepeatHandler(KeyHandler handler) {
			this.keyRepeatHandler = handler;
			return this;
		}

		public Builder withTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder withVSync() {
			this.vsync = true;
			return this;
		}

		public GLWindow build() {
			window = glfwCreateWindow(width, height, title, NULL, NULL);
			if (window == NULL)
				throw new GLException("Unable to create window");

			glfwMakeContextCurrent(window);
			glfwSwapInterval(vsync ? 1 : 0);
			GL.createCapabilities();

			if (debug) {
				GLUtil.setupDebugMessageCallback();
			}

			int error = glGetError();
			if (error != GL_NO_ERROR)
				throw new GLException("Error building OpenGL window : " + error);

			return new GLWindow(this);
		}
	}

}
