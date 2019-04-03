/**
 * 
 */
package org.yah.games.gameoflife.opengl.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.yah.games.gameoflife.opengl.GLApplication;
import org.yah.games.gameoflife.opengl.GLObject;
import org.yah.games.gameoflife.opengl.ShaderCompileException;


/**
 * @author Oodrive
 * @created 2019/03/27
 */
public class Shader extends GLObject {

	public enum Type {
		VERTEX(GL_VERTEX_SHADER), FRAGMENT(GL_FRAGMENT_SHADER);
		
		private final int glType;

		private Type(int glType) {
			this.glType = glType;
		}
	}

	private final Type type;

	private Shader(int shaderId, Type type) {
		super(shaderId);
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public void delete() {
		glDeleteShader(id);
	}

	private static String readResource(String resource) throws IOException {
		ClassLoader classLoader = GLApplication.class.getClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(resource)) {
			if (is == null)
				throw new FileNotFoundException("Resource " + resource + " not found in classpath");
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		}
	}

	private static Shader load(Type type, String resource) throws ShaderCompileException, IOException {
		int shaderId = glCreateShader(type.glType);
		glShaderSource(shaderId, readResource(resource));
		glCompileShader(shaderId);
		int status = glGetShaderi(shaderId, GL_COMPILE_STATUS);
		if (status == GL_FALSE) {
			String log = glGetShaderInfoLog(shaderId);
			throw new ShaderCompileException(log);
		}
		return new Shader(shaderId, type);
	}

	public static Shader vertexShader(String resource) throws ShaderCompileException, IOException {
		return load(Type.VERTEX, resource);
	}

	public static Shader fragmentShader(String resource) throws ShaderCompileException, IOException {
		return load(Type.FRAGMENT, resource);
	}

}
