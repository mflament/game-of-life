package org.yah.games.gameoflife.opengl.shader.uniform;

import static org.lwjgl.opengl.GL20.glUniform3f;

public final class Uniform3f extends Uniform {

	public Uniform3f(int location) {
		super(location);
	}

	public void setValue(float v0, float v1, float v2) {
		glUniform3f(location, v0, v1, v2);
	}
}
