/**
 * 
 */
package org.yah.games.gameoflife.opengl.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

import java.util.ArrayList;
import java.util.List;

import org.yah.games.gameoflife.opengl.GLObject;
import org.yah.games.gameoflife.opengl.ProgramLinkException;


/**
 * @author Oodrive
 * @created 2019/03/29
 */
public class Program extends GLObject {

	public Program(int programId) {
		super(programId);
	}
	
	public void use() {
		glUseProgram(id);
	}

	public void unuse() {
		glUseProgram(GL_ZERO);
	}
	
	public int findAttributeLocation(String attributeName) {
		return glGetAttribLocation(id, attributeName);
	}
	
	public int getAttributeLocation(String attributeName) {
		int loc = findAttributeLocation(attributeName);
		if (loc == -1)
			throw new IllegalArgumentException("Unknown attribute " + attributeName);
		return loc;
	}
	
	public void delete() {
		glDeleteProgram(id);
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final int programId;

		private final List<Shader> shaders = new ArrayList<>();

		public Builder() {
			programId = glCreateProgram();
		}

		public Builder with(Shader shader) {
			shaders.add(shader);
			glAttachShader(programId, shader.getId());
			return this;
		}
		
		public Builder withFragDataLocation(int colorNumber, String outputName) {
			glBindFragDataLocation(programId, colorNumber, outputName);
			return this;
		}

		public Program build() {
			try {
				glLinkProgram(programId);
				int linkStatus = glGetProgrami(programId, GL_LINK_STATUS);
				if (linkStatus == GL_FALSE) {
					String log = glGetProgramInfoLog(programId);
					throw new ProgramLinkException(log);
				}
				return new Program(programId);
			} finally {
				shaders.forEach(s -> {
					glDetachShader(programId, s.getId());
					s.delete();
				});
			}
		}
	}
}
