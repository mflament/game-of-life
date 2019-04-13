package org.yah.games.gameoflife.opengl.shader.uniform;

public abstract class Uniform {

	protected final int location;

	public Uniform(int location) {
		super();
		this.location = location;
	}

}
