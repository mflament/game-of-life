/**
 * 
 */
package org.yah.games.opengl;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.yah.games.opengl.shader.Program;
import org.yah.games.opengl.vao.ComponentType;
import org.yah.games.opengl.vao.VAO;
import org.yah.games.opengl.vbo.BufferAccess;
import org.yah.games.opengl.vbo.VBO;
import org.yah.games.opengl.vbo.BufferAccess.Frequency;
import org.yah.games.opengl.vbo.BufferAccess.Nature;

/**
 * @author Marc Flament
 * @created 2019/03/04
 */
public abstract class GL2DApplication extends GLApplication {

	private final float[] QUAD_VERTICES = { -1, -1, 0, 1, //
			-1, 1, 0, 0, //
			1, 1, 1, 0, //
			-1, -1, 0, 1, //
			1, 1, 1, 0, //
			1, -1, 1, 1 };

	private VBO vbo;

	private VAO vao;

	private Program renderProgram;

	@Override
	protected void loadResources() {
		vbo = VBO.builder().withData(QUAD_VERTICES, BufferAccess.from(Frequency.STATIC, Nature.DRAW)).build();
		renderProgram = createRenderProgram();
		vao = VAO.builder(renderProgram, vbo)
			.withAttribute("position", 2, ComponentType.FLOAT, false, ComponentType.FLOAT.sizeOf(4), 0)
			.withAttribute("vTexCoordinate", 2, ComponentType.FLOAT, false, ComponentType.FLOAT.sizeOf(4),
					ComponentType.FLOAT.sizeOf(2))
			.build();
		vao.bind();
	}

	@Override
	protected void frameBufferResized(int width, int height) {
		super.frameBufferResized(width, height);
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		vao.delete();
		renderProgram.delete();
		vbo.delete();
	}

	protected abstract Program createRenderProgram();

	@Override
	protected void render() {
		renderProgram.use();
		draw();
	}

	protected final void draw() {
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}

}
