/**
 * 
 */
package org.yah.games.gameoflife.opengl;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.yah.games.gameoflife.opengl.shader.Program;
import org.yah.games.gameoflife.opengl.shader.Shader;
import org.yah.games.gameoflife.opengl.vao.VAO;
import org.yah.games.gameoflife.opengl.vbo.BufferAccess;
import org.yah.games.gameoflife.opengl.vbo.BufferAccess.Frequency;
import org.yah.games.gameoflife.opengl.vbo.BufferAccess.Nature;
import org.yah.games.gameoflife.opengl.vbo.VBO;

/**
 * @author Oodrive
 * @created 2019/03/04
 */
public abstract class GL2DApplication extends GLApplication {

	private final float[] QUAD_VERTICES = { -1, -1, //
			-1, 1, //
			1, 1, //
			-1, -1, //
			1, 1, //
			1, -1 };

	private VAO vao;

	@Override
	protected void loadResources() {
		VBO vbo = VBO.builder().withData(QUAD_VERTICES, BufferAccess.from(Frequency.STATIC, Nature.DRAW)).build();
		Program program = Program.builder()
			.with(Shader.fragmentShader("shaders/2d.fs.glsl"))
			.with(Shader.vertexShader("shaders/2d.vs.glsl"))
			.build();
		vao = VAO.builder(program, vbo).withAttribute("position", 2).build();
		vao.bind();
	}

	@Override
	protected void render() {
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}

}
