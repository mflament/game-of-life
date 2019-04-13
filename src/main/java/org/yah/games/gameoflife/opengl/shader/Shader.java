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
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.yah.games.gameoflife.opengl.GLApplication;
import org.yah.games.gameoflife.opengl.GLObject;
import org.yah.games.gameoflife.opengl.ShaderCompileException;

/**
 * @author Marc Flament
 * @created 2019/03/27
 */
public class Shader extends GLObject {

	public enum Type {
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER);

		private final int glType;

		private Type(int glType) {
			this.glType = glType;
		}
	}

	private static final Pattern HOLDER_PATTERN = Pattern.compile("\\$\\{([\\w\\.\\-]+)\\}");

	private final Type type;

	private Shader(int shaderId, Type type) {
		super(shaderId);
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@Override
	public void delete() {
		glDeleteShader(id);
	}

	private static String readResource(String resource) {
		ClassLoader classLoader = GLApplication.class.getClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(resource)) {
			if (is == null)
				throw new FileNotFoundException("Resource " + resource + " not found in classpath");
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static Shader load(Type type, String resource, Map<String, ?> holderValues) {
		int shaderId = glCreateShader(type.glType);
		String source = readResource(resource);
		if (holderValues != null) {
			source = replaceHolders(source, holderValues);
		}
		glShaderSource(shaderId, source);
		glCompileShader(shaderId);
		int status = glGetShaderi(shaderId, GL_COMPILE_STATUS);
		if (status == GL_FALSE) {
			String log = glGetShaderInfoLog(shaderId);
			throw new ShaderCompileException(log);
		}
		return new Shader(shaderId, type);
	}

	private static String replaceHolders(String source, Map<String, ?> holderValues) {
		Matcher matcher = HOLDER_PATTERN.matcher(source);
		StringBuilder sb = new StringBuilder();
		int position = 0;
		while (matcher.find(position)) {
			String holderName = matcher.group(1);
			Object value = holderValues.get(holderName);
			if (value == null) {
				throw new IllegalArgumentException("Unresolved holder " + holderName);
			}
			sb.append(source.substring(position, matcher.start()));
			sb.append(value);
			position = matcher.end();
		}
		sb.append(source.substring(position, source.length()));
		return sb.toString();
	}

	public static Shader vertexShader(String resource) {
		return load(Type.VERTEX, resource, null);
	}

	public static Shader vertexShader(String resource, Map<String, ?> holderValues) {
		return load(Type.VERTEX, resource, holderValues);
	}

	public static Shader fragmentShader(String resource) {
		return load(Type.FRAGMENT, resource, null);
	}

	public static Shader fragmentShader(String resource, Map<String, ?> holderValues) {
		return load(Type.FRAGMENT, resource, holderValues);
	}

}
