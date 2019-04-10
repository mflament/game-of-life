package org.yah.games.gameoflife.opengl.vao;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_DOUBLE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_SHORT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_2_10_10_10_REV;
import static org.lwjgl.opengl.GL30.GL_HALF_FLOAT;
import static org.lwjgl.opengl.GL30.GL_UNSIGNED_INT_10F_11F_11F_REV;
import static org.lwjgl.opengl.GL33.GL_INT_2_10_10_10_REV;
import static org.lwjgl.opengl.GL41.GL_FIXED;

public enum ComponentType {
	BYTE(GL_BYTE),
	UNSIGNED_BYTE(GL_UNSIGNED_BYTE),
	SHORT(GL_SHORT),
	UNSIGNED_SHORT(GL_UNSIGNED_SHORT),
	INT(GL_INT),
	UNSIGNED_INT(GL_UNSIGNED_INT),

	HALF_FLOAT(GL_HALF_FLOAT),
	FLOAT(GL_FLOAT),
	DOUBLE(GL_DOUBLE),
	FIXED(GL_FIXED),
	INT_2_10_10_10_REV(GL_INT_2_10_10_10_REV),
	UNSIGNED_INT_2_10_10_10_REV(GL_UNSIGNED_INT_2_10_10_10_REV),
	UNSIGNED_INT_10F_11F_11F_REV(GL_UNSIGNED_INT_10F_11F_11F_REV);

	private final int glType;

	private ComponentType(int glTarget) {
		this.glType = glTarget;
	}

	public int getGlType() {
		return glType;
	}
}