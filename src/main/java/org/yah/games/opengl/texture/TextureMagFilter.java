package org.yah.games.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;

public enum TextureMagFilter {
	NEAREST(GL_NEAREST),
	LINEAR(GL_LINEAR);

	private final int glType;

	TextureMagFilter(int glType) {
		this.glType = glType;
	}
	
	public int getGlType() {
		return glType;
	}
}
