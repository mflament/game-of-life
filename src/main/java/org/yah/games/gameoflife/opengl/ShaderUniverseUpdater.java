package org.yah.games.gameoflife.opengl;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.yah.games.gameoflife.rule.Rule;
import org.yah.games.gameoflife.universe.Universe;
import org.yah.games.gameoflife.universe.updater.UniverseUpdater;
import org.yah.games.opengl.fbo.FBO;
import org.yah.games.opengl.shader.Program;
import org.yah.games.opengl.shader.Shader;

public class ShaderUniverseUpdater implements UniverseUpdater, Closeable {

	private final FBO fbo;

	private final Program computeProgram;

	public ShaderUniverseUpdater(Rule rule) {
		fbo = FBO.builder().build();

		Map<String, String> holderValue = Collections.singletonMap("rule", RuleToArray.createFloatArrays(rule));
		computeProgram = Program.builder()
			.with(Shader.vertexShader("shaders/render.vs.glsl"))
			.with(Shader.fragmentShader("shaders/compute.fs.glsl", holderValue))
			.build();
	}

	@Override
	public void close() throws IOException {
		fbo.delete();
		computeProgram.delete();
	}

	@Override
	public void update(Universe from, Universe to) {
		TextureUniverse fromTextureUniverse = (TextureUniverse) from;
		TextureUniverse toTextureUniverse = (TextureUniverse) to;

		computeProgram.use();
		fbo.bind();
		fbo.attach(toTextureUniverse.getTexture());

		fromTextureUniverse.bind();
		glDrawArrays(GL_TRIANGLES, 0, 6);

		fbo.checkCompletion();
		fbo.unbind();
	}

}
