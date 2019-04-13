package org.yah.games.gameoflife.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;

public enum TextureMinFilter {
	NEAREST(GL_NEAREST),
	LINEAR(GL_LINEAR),
	NEAREST_MIPMAP_NEAREST(GL_NEAREST_MIPMAP_NEAREST),
	LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST),
	NEAREST_MIPMAP_LINEAR(GL_NEAREST_MIPMAP_LINEAR),
	LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR);

	private final int glType;

	TextureMinFilter(int glType) {
		this.glType = glType;
	}
	
	public int getGlType() {
		return glType;
	}
}
