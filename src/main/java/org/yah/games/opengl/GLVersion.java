package org.yah.games.opengl;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public final class GLVersion {

	private GLVersion() {}

	public static void printVersions() {
		// Setup an error callback.
		GLFWErrorCallback.create(GLFWErrorCallback.createPrint(System.err)).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		// glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		// glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		// glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

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
			predicate = GLVersion::isPublicInstanceField;
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
		printVersions();
	}

}
