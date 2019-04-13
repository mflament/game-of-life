package org.yah.games.gameoflife.opengl.fbo;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import org.yah.games.gameoflife.opengl.GLException;
import org.yah.games.gameoflife.opengl.GLObject;
import org.yah.games.gameoflife.opengl.texture.Texture2D;

public class FBO extends GLObject {

	public FBO(Builder builder) {
		super(builder.id);
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void checkCompletion() {
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			throw new GLException("framebuffer is incomplete");
	}

	public void attach(Texture2D texture) {
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getId(), 0);
	}

	@Override
	public void delete() {
		glDeleteFramebuffers(id);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		public final int id;

		public Builder() {
			id = glGenFramebuffers();
			glBindFramebuffer(GL_FRAMEBUFFER, id);
		}

		public FBO build() {
			glBindFramebuffer(GL_FRAMEBUFFER, 0);
			return new FBO(this);
		}
	}
}
