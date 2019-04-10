package org.yah.games.gameoflife.opengl.vao;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.LinkedHashSet;
import java.util.Set;

import org.yah.games.gameoflife.opengl.GLObject;
import org.yah.games.gameoflife.opengl.shader.Program;
import org.yah.games.gameoflife.opengl.vbo.VBO;

public class VAO extends GLObject {

	private final Program program;

	private final VBO vbo;

	private final int[] attributeIndices;

	public VAO(Builder builder) {
		super(builder.id);
		this.program = builder.program;
		this.vbo = builder.vbo;
		this.attributeIndices = builder.attributesIndices.stream().mapToInt(Integer::intValue).toArray();
	}

	public void bind() {
		program.use();
		glBindVertexArray(id);
		for (int i = 0; i < attributeIndices.length; i++) {
			glEnableVertexAttribArray(attributeIndices[i]);
		}
	}

	public VBO getVbo() {
		return vbo;
	}

	public Program getProgram() {
		return program;
	}

	public void delete() {
		glDeleteVertexArrays(id);
	}

	public static Builder builder(Program program, VBO vbo) {
		return new Builder(program, vbo);
	}

	public static class Builder {

		private final int id;

		private final Program program;

		private final VBO vbo;

		private final Set<Integer> attributesIndices = new LinkedHashSet<>();

		public Builder(Program program, VBO vbo) {
			this.program = program;
			this.vbo = vbo;
			vbo.bind();
			id = glGenVertexArrays();
			glBindVertexArray(id);
		}

		public Builder withAttribute(String name) {
			return withAttribute(name, 3, ComponentType.FLOAT, false, 0, 0);
		}

		public Builder withAttribute(String name, int size) {
			return withAttribute(name, size, ComponentType.FLOAT, false, 0, 0);
		}

		public Builder withAttribute(String name, int size, ComponentType componentType) {
			return withAttribute(name, size, componentType, false, 0, 0);
		}

		public Builder withAttribute(String name, int size, ComponentType componentType, boolean normalized, int stride,
				int pointer) {
			int index = program.getAttributeLocation(name);
			glVertexAttribPointer(index, size, componentType.getGlType(), normalized, stride, pointer);
			attributesIndices.add(index);
			return this;
		}

		public VAO build() {
			return new VAO(this);
		}
	}
}
