package org.yah.games.opengl.vbo;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

public enum BufferTarget {
	ARRAY(GL_ARRAY_BUFFER);

	private final int glTarget;

	private BufferTarget(int glTarget) {
		this.glTarget = glTarget;
	}

	public int getGlTarget() {
		return glTarget;
	}
}