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
	BYTE(GL_BYTE, 1),
	UNSIGNED_BYTE(GL_UNSIGNED_BYTE, 1),
	SHORT(GL_SHORT, 2),
	UNSIGNED_SHORT(GL_UNSIGNED_SHORT, 2),
	INT(GL_INT, 4),
	UNSIGNED_INT(GL_UNSIGNED_INT, 4),

	HALF_FLOAT(GL_HALF_FLOAT, 4),
	FLOAT(GL_FLOAT, 4),
	DOUBLE(GL_DOUBLE, 8),
	FIXED(GL_FIXED, 4),
	INT_2_10_10_10_REV(GL_INT_2_10_10_10_REV, 4),
	UNSIGNED_INT_2_10_10_10_REV(GL_UNSIGNED_INT_2_10_10_10_REV, 4),
	UNSIGNED_INT_10F_11F_11F_REV(GL_UNSIGNED_INT_10F_11F_11F_REV, 4);

	private final int glType;

	/**
	 * size in bytes
	 */
	private final int size;

	private ComponentType(int glTarget, int size) {
		this.glType = glTarget;
		this.size = size;
	}

	public int getGlType() {
		return glType;
	}

	public int getSize() {
		return size;
	}

	public int sizeOf(int count) {
		return size * count;
	}

}