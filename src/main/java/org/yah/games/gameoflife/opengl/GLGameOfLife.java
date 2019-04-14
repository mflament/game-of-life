package org.yah.games.gameoflife.opengl;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.yah.games.gameoflife.AbstractGameOfLife;
import org.yah.games.gameoflife.UserConfiguration;
import org.yah.games.gameoflife.universe.Universe;
import org.yah.games.gameoflife.universe.bytebuffer.ByteBufferUniverse;
import org.yah.games.gameoflife.universe.updater.UniverseUpdater;
import org.yah.games.opengl.shader.Program;
import org.yah.games.opengl.shader.Shader;
import org.yah.games.opengl.texture.Texture2D;
import org.yah.games.opengl.vao.ComponentType;
import org.yah.games.opengl.vao.VAO;
import org.yah.games.opengl.vbo.BufferAccess;
import org.yah.games.opengl.vbo.BufferAccess.Frequency;
import org.yah.games.opengl.vbo.BufferAccess.Nature;
import org.yah.games.opengl.vbo.VBO;
import org.yah.games.opengl.window.GLWindow;

public class GLGameOfLife extends AbstractGameOfLife {

	private static final String TITLE = "Game of Life (OpenGL)";

	private final float[] QUAD_VERTICES = { -1, -1, 0, 1, //
			-1, 1, 0, 0, //
			1, 1, 1, 0, //
			-1, -1, 0, 1, //
			1, 1, 1, 0, //
			1, -1, 1, 1 };

	private GLWindow window;

	private VBO vbo;

	private VAO vao;

	private Program renderProgram;

	/**
	 * used only if updater is not a {@link ShaderUniverseUpdater}
	 */
	private Texture2D universeTexture;

	private Consumer<Universe> universeRenderPreparator;

	public static void main(String[] args) {
		new GLGameOfLife(UserConfiguration.parse(args)).run();
	}

	private GLGameOfLife(UserConfiguration configuration) {
		super(configuration);
	}

	@Override
	protected void createWindow() {
		int universeSize = configuration.getUniverseSize();
		window = GLWindow.builder()
			.withTitle(TITLE)
			.withWidth(universeSize)
			.withHeight(universeSize)
			.withKeyPressHandler(this::keyPressed)
			.build();
		window.show();

		GL.createCapabilities();

		renderProgram = Program.builder()
			.with(Shader.vertexShader("shaders/render.vs.glsl"))
			.with(Shader.fragmentShader("shaders/render.fs.glsl"))
			.build();

		vbo = VBO.builder().withData(QUAD_VERTICES, BufferAccess.from(Frequency.STATIC, Nature.DRAW)).build();
		vao = VAO.builder(renderProgram, vbo)
			.withAttribute("position", 2, ComponentType.FLOAT, false, ComponentType.FLOAT.sizeOf(4), 0)
			.withAttribute("vTexCoordinate", 2, ComponentType.FLOAT, false, ComponentType.FLOAT.sizeOf(4),
					ComponentType.FLOAT.sizeOf(2))
			.build();
		vao.bind();

		glViewport(0, 0, universeSize, universeSize);
	}

	@Override
	protected boolean isCloseRequested() {
		return window.isCloseRequested();
	}

	@Override
	protected UniverseUpdater createUniverseUpdater() {
		String updaterName = getUpdaterName();
		if (updaterName.equals("shader")) {
			return new ShaderUniverseUpdater(configuration.getRule());
		}
		return super.createUniverseUpdater();
	}

	@Override
	protected Universe createUniverse(UniverseUpdater universeUpdater, int universeSize) {
		if (universeUpdater instanceof ShaderUniverseUpdater) {
			universeRenderPreparator = u -> ((TextureUniverse) u).bind();
			return new TextureUniverse(universeSize, universeSize);
		} else {
			if (universeTexture == null) {
				//create the universe texture used for rendering (only once)
				universeTexture = TextureUniverse.createUniverseTexture(universeSize, universeSize);
				universeRenderPreparator = u -> {
					universeTexture.bind();
					TextureUniverse.updateData(((ByteBufferUniverse) u).getBuffer(), universeTexture);
				};
			}
			return new ByteBufferUniverse(universeSize, universeSize);
		}
	}

	@Override
	protected String getDefaultUpdaterName() {
		return "shader";
	}

	@Override
	protected void pollEvents() {
		glfwPollEvents();
	}

	@Override
	protected void doRender(Universe universe) {
		renderProgram.use();
		universeRenderPreparator.accept(universe);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		window.swapBuffers();
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		if (universeTexture != null)
			universeTexture.delete();
		vao.delete();
		vbo.delete();
		renderProgram.delete();
		GLFW.glfwTerminate();
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void updateTitle(String title) {
		window.setTitle(title);
	}

	protected void keyPressed(int key, int scancode, int mods) {
		switch (key) {
		case GLFW_KEY_SPACE:
			toggleUpdate();
			break;
		case GLFW_KEY_R:
			randomize();
			break;
		case GLFW_KEY_C:
			clear();
			break;
		default:
			break;
		}
	}

}
