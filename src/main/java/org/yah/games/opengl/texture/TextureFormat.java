package org.yah.games.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_INDEX;
import static org.lwjgl.opengl.GL12.GL_BGR;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL30.GL_BGRA_INTEGER;
import static org.lwjgl.opengl.GL30.GL_BGR_INTEGER;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL;
import static org.lwjgl.opengl.GL30.GL_RED_INTEGER;
import static org.lwjgl.opengl.GL30.GL_RG;
import static org.lwjgl.opengl.GL30.GL_RGBA_INTEGER;
import static org.lwjgl.opengl.GL30.GL_RGB_INTEGER;
import static org.lwjgl.opengl.GL30.GL_RG_INTEGER;

public enum TextureFormat {
	RED(GL_RED),
	RG(GL_RG),
	RGB(GL_RGB),
	BGR(GL_BGR),
	RGBA(GL_RGBA),
	BGRA(GL_BGRA),
	RED_INTEGER(GL_RED_INTEGER),
	RG_INTEGER(GL_RG_INTEGER),
	RGB_INTEGER(GL_RGB_INTEGER),
	BGR_INTEGER(GL_BGR_INTEGER),
	RGBA_INTEGER(GL_RGBA_INTEGER),
	BGRA_INTEGER(GL_BGRA_INTEGER),
	STENCIL_INDEX(GL_STENCIL_INDEX),
	DEPTH_COMPONENT(GL_DEPTH_COMPONENT),
	DEPTH_STENCIL(GL_DEPTH_STENCIL);

	private final int glType;

	TextureFormat(int glType) {
		this.glType = glType;
	}

	public int getGlType() {
		return glType;
	}
}
