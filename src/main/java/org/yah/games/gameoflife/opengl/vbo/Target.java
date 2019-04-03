package org.yah.games.gameoflife.opengl.vbo;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

public enum Target {
	ARRAY(GL_ARRAY_BUFFER);

	private final int glTarget;

	private Target(int glTarget) {
		this.glTarget = glTarget;
	}

	public int getGlTarget() {
		return glTarget;
	}
}