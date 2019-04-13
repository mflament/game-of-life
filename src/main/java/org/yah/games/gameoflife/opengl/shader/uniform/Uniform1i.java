package org.yah.games.gameoflife.opengl.shader.uniform;

import static org.lwjgl.opengl.GL20.glUniform1i;

public final class Uniform1i extends Uniform {

	public Uniform1i(int location) {
		super(location);
	}

	public void setValue(int v) {
		glUniform1i(location, v);
	}
}
