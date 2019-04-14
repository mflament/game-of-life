package org.yah.games.opengl.texture;

import static org.lwjgl.opengl.GL11.*;

public enum TextureSwizzle {

	RED(GL_RED),
	GREEN(GL_GREEN),
	BLUE(GL_BLUE),
	ALPHA(GL_ALPHA),
	ZERO(GL_ZERO),
	ONE(GL_ONE);

	private final int glType;

	TextureSwizzle(int glType) {
		this.glType = glType;
	}

	public int getGlType() {
		return glType;
	}

}
