package org.yah.games.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_SHORT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_BYTE_2_3_3_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_BYTE_3_3_2;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_10_10_10_2;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_2_10_10_10_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_4_4_4_4;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_5_5_5_1;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_5_6_5;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_5_6_5_REV;
import static org.lwjgl.opengl.GL30.GL_HALF_FLOAT;

public enum TextureDataType {
	UNSIGNED_BYTE(GL_UNSIGNED_BYTE),
	BYTE(GL_BYTE),
	UNSIGNED_SHORT(GL_UNSIGNED_SHORT),
	SHORT(GL_SHORT),
	UNSIGNED_INT(GL_UNSIGNED_INT),
	INT(GL_INT),
	HALF_FLOAT(GL_HALF_FLOAT),
	FLOAT(GL_FLOAT),
	UNSIGNED_BYTE_3_3_2(GL_UNSIGNED_BYTE_3_3_2),
	UNSIGNED_BYTE_2_3_3_REV(GL_UNSIGNED_BYTE_2_3_3_REV),
	UNSIGNED_SHORT_5_6_5(GL_UNSIGNED_SHORT_5_6_5),
	UNSIGNED_SHORT_5_6_5_REV(GL_UNSIGNED_SHORT_5_6_5_REV),
	UNSIGNED_SHORT_4_4_4_4(GL_UNSIGNED_SHORT_4_4_4_4),
	UNSIGNED_SHORT_4_4_4_4_REV(GL_UNSIGNED_SHORT_4_4_4_4_REV),
	UNSIGNED_SHORT_5_5_5_1(GL_UNSIGNED_SHORT_5_5_5_1),
	UNSIGNED_SHORT_1_5_5_5_REV(GL_UNSIGNED_SHORT_1_5_5_5_REV),
	UNSIGNED_INT_8_8_8_8(GL_UNSIGNED_INT_8_8_8_8),
	UNSIGNED_INT_8_8_8_8_REV(GL_UNSIGNED_INT_8_8_8_8_REV),
	UNSIGNED_INT_10_10_10_2(GL_UNSIGNED_INT_10_10_10_2),
	UNSIGNED_INT_2_10_10_10_REV(GL_UNSIGNED_INT_2_10_10_10_REV);

	private final int glType;

	TextureDataType(int glType) {
		this.glType = glType;
	}

	public int getGlType() {
		return glType;
	}
}
