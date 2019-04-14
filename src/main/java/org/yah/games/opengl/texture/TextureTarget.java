package org.yah.games.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_1D_ARRAY;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_RECTANGLE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
import static org.lwjgl.opengl.GL40.GL_TEXTURE_CUBE_MAP_ARRAY;

public enum TextureTarget {
	
	TEXTURE_2D(GL_TEXTURE_2D),
	TEXTURE_3D(GL_TEXTURE_3D),
	TEXTURE_1D_ARRAY(GL_TEXTURE_1D_ARRAY),
	TEXTURE_2D_ARRAY(GL_TEXTURE_2D_ARRAY),
	TEXTURE_RECTANGLE(GL_TEXTURE_RECTANGLE),
	TEXTURE_CUBE_MAP(GL_TEXTURE_CUBE_MAP),
	TEXTURE_CUBE_MAP_ARRAY(GL_TEXTURE_CUBE_MAP_ARRAY),
	TEXTURE_BUFFER(GL_TEXTURE_BUFFER),
	TEXTURE_2D_MULTISAMPLE(GL_TEXTURE_2D_MULTISAMPLE),
	TEXTURE_2D_MULTISAMPLE_ARRAY(GL_TEXTURE_2D_MULTISAMPLE_ARRAY);

	private final int glType;

	TextureTarget(int glType) {
		this.glType = glType;
	}

	public int getGlType() {
		return glType;
	}

}
