package org.yah.games.gameoflife.opengl;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DEPTH_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_ALT;
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
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;

public abstract class GLApplication {

	private long window;

	private Color4f clearColor;

	public void start() {
		setup();

		try {
			loop();
		} finally {
			destroy();
		}
	}

	private void setup() {
		// Setup an error callback.
		GLFWErrorCallback.createThrow().set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		glfwWindowHint(GLFW_DEPTH_BITS, 24);
		glfwWindowHint(GLFW_STENCIL_BITS, 8);

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		if (isDebugEnabled()) {
			glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
		}

		window = glfwCreateWindow(640, 480, getTitle(), NULL, NULL);
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

		int error = glGetError();
		if (error != GL_NO_ERROR) {
			throw new GLException("Error initializing application: " + error);
		}
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

	private void switchFullScreen() {
		long monitor = glfwGetWindowMonitor(window);
		if (monitor == NULL) {
			System.out.println("switchFullScreen");
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, 0, 0, 0);
		} else {
			System.out.println("switchWindowed from " + monitor);
		}

	}

	protected abstract void render();

	protected void frameBufferResized(int width, int height) {
		glViewport(0, 0, width, height);
	}

	protected void keyPressed(int key, int scancode, int mods) {}

	protected void keyReleased(int key, int scancode, int mods) {
		switch (key) {
		case GLFW_KEY_ESCAPE:
			glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			break;
		case GLFW_KEY_ENTER:
			if ((mods & GLFW_MOD_ALT) != 0) {
				switchFullScreen();
			}
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

	public static void checkVersion() {
		// Setup an error callback.
		GLFWErrorCallback.create(GLFWErrorCallback.createPrint(System.err)).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		long window = glfwCreateWindow(800, 600, "Test", NULL, NULL);
		if (window == NULL)
			throw new IllegalStateException("Unable to create window");

		glfwMakeContextCurrent(window);

		GLCapabilities capabilities = GL.createCapabilities();
		System.out.println(dump(capabilities, "OpenGL.*"));

		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private static String dump(GLCapabilities capabilities, String regex) {
		Predicate<Field> predicate;
		if (regex == null) {
			predicate = GLApplication::isPublicInstanceField;
		} else {
			Pattern pattern = Pattern.compile(regex);
			predicate = f -> isPublicInstanceField(f) && pattern.matcher(f.getName()).matches();
		}

		Field[] declaredFields = capabilities.getClass().getDeclaredFields();
		Arrays.sort(declaredFields, Comparator.comparing(Field::getName));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < declaredFields.length; i++) {
			if (predicate.test(declaredFields[i])) {
				Object object;
				try {
					object = declaredFields[i].get(capabilities);
				} catch (Exception e) {
					System.err.println("Error getting field " + declaredFields[i].getName());
					e.printStackTrace();
					continue;
				}
				sb.append(declaredFields[i].getName()).append(" : ").append(object).append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

	private static boolean isPublicInstanceField(Field field) {
		int modifiers = field.getModifiers();
		return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
	}

	public static void main(String[] args) {
		checkVersion();
	}
}
