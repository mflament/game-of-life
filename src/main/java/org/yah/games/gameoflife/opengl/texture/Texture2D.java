package org.yah.games.gameoflife.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_BORDER_COLOR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterfv;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexParameteriv;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_A;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_B;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_G;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_R;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_RGBA;

import java.nio.ByteBuffer;

import org.yah.games.gameoflife.opengl.GLObject;

public final class Texture2D extends GLObject {

	private final TextureInternalFormat internalFormat;

	private final int width, height;

	private Texture2D(Builder builder) {
		super(builder.id);
		this.width = builder.width;
		this.height = builder.height;
		this.internalFormat = builder.internalFormat;
	}

	public TextureInternalFormat getInternalFormat() {
		return internalFormat;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void updateData(TextureFormat format, TextureDataType dataType, ByteBuffer data) {
		glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, format.getGlType(), dataType.getGlType(), data);
	}

	public void updateData(int level, int xOffset, int yOffset, int width, int height, TextureFormat format,
			TextureDataType dataType, ByteBuffer data) {
		glTexSubImage2D(GL_TEXTURE_2D, level, xOffset, yOffset, width, height, format.getGlType(), dataType.getGlType(),
				data);
	}

	@Override
	public void delete() {
		glDeleteTextures(id);
	}

	public static Builder builder(int width, int height) {
		return new Builder(width, height);
	}

	public static Builder builder(int width, int height, TextureTarget target) {
		return new Builder(width, height, target);
	}

	public final static class Builder {

		private final int id;

		private final TextureTarget target;

		public final int width;

		public final int height;

		public TextureInternalFormat internalFormat = TextureInternalFormat.RGBA;

		private boolean dataSet;

		public Builder(int width, int height) {
			this(width, height, TextureTarget.TEXTURE_2D);
		}

		public Builder(int width, int height, TextureTarget target) {
			id = glGenTextures();
			this.target = target;
			this.width = width;
			this.height = height;
			glBindTexture(target.getGlType(), id);
		}

		public Builder withInternalFormat(TextureInternalFormat internalFormat) {
			this.internalFormat = internalFormat;
			return this;
		}

		public Builder wrapS(TextureWrap wrapMode) {
			wrap(GL_TEXTURE_WRAP_S, wrapMode);
			return this;
		}

		public Builder wrapT(TextureWrap wrapMode) {
			wrap(GL_TEXTURE_WRAP_T, wrapMode);
			return this;
		}

		public Builder wrapR(TextureWrap wrapMode) {
			wrap(GL_TEXTURE_WRAP_R, wrapMode);
			return this;
		}

		public Builder swizzleR(TextureSwizzle swizzle) {
			swizzle(GL_TEXTURE_SWIZZLE_R, swizzle);
			return this;
		}

		public Builder swizzleG(TextureSwizzle swizzle) {
			swizzle(GL_TEXTURE_SWIZZLE_G, swizzle);
			return this;
		}

		public Builder swizzleB(TextureSwizzle swizzle) {
			swizzle(GL_TEXTURE_SWIZZLE_B, swizzle);
			return this;
		}

		public Builder swizzleA(TextureSwizzle swizzle) {
			swizzle(GL_TEXTURE_SWIZZLE_A, swizzle);
			return this;
		}

		public Builder swizzleRGBA(TextureSwizzle sr, TextureSwizzle sg, TextureSwizzle sb, TextureSwizzle sa) {
			glTexParameteriv(target.getGlType(), GL_TEXTURE_SWIZZLE_RGBA,
					new int[] { sr.getGlType(), sg.getGlType(), sb.getGlType(), sa.getGlType() });
			return this;
		}

		public Builder minFilter(TextureMinFilter filter) {
			glTexParameteri(target.getGlType(), GL_TEXTURE_MIN_FILTER, filter.getGlType());
			return this;
		}

		public Builder magFilter(TextureMagFilter filter) {
			glTexParameteri(target.getGlType(), GL_TEXTURE_MAG_FILTER, filter.getGlType());
			return this;
		}

		public Builder borderColor(float r, float g, float b, float a) {
			glTexParameterfv(target.getGlType(), GL_TEXTURE_BORDER_COLOR, new float[] { r, g, b, a });
			return this;
		}

		public Builder withData(TextureFormat format, TextureDataType dataType, ByteBuffer data) {
			return withData(0, format, dataType, data);
		}

		public Builder withData(int level, TextureFormat dataFormat, TextureDataType dataType, ByteBuffer data) {
			glTexImage2D(target.getGlType(), level, internalFormat.getGlType(), width, height, 0,
					dataFormat.getGlType(), dataType.getGlType(), data);
			dataSet = true;
			return this;
		}

		private void wrap(int coord, TextureWrap wrapMode) {
			glTexParameteri(target.getGlType(), coord, wrapMode.getGlType());
		}

		private void swizzle(int swizzleName, TextureSwizzle swizzle) {
			glTexParameteri(target.getGlType(), swizzleName, swizzle.getGlType());
		}

		public Texture2D build() {
			if (!dataSet) {
				glTexImage2D(target.getGlType(), 0, internalFormat.getGlType(), width, height, 0,
						TextureFormat.RGBA.getGlType(), TextureDataType.UNSIGNED_BYTE.getGlType(), 0);
			}
			return new Texture2D(this);
		}
	}

}
