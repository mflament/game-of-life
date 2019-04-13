package org.yah.games.gameoflife.opengl;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.yah.games.gameoflife.counter.TimeTracker;
import org.yah.games.gameoflife.java2d.rule.RuleConfiguration;
import org.yah.games.gameoflife.opengl.fbo.FBO;
import org.yah.games.gameoflife.opengl.shader.Program;
import org.yah.games.gameoflife.opengl.shader.Shader;
import org.yah.games.gameoflife.opengl.texture.Texture2D;
import org.yah.games.gameoflife.opengl.texture.TextureDataType;
import org.yah.games.gameoflife.opengl.texture.TextureFormat;
import org.yah.games.gameoflife.opengl.texture.TextureInternalFormat;
import org.yah.games.gameoflife.opengl.texture.TextureMagFilter;
import org.yah.games.gameoflife.opengl.texture.TextureMinFilter;
import org.yah.games.gameoflife.opengl.texture.TextureWrap;

public class GameOfLife extends GL2DApplication {

	private static final int UNIVERSE_SIZE = 1024;

	private static final boolean DEBUG = false;

	private static final boolean VSYNC = true;

	private static final float RANDOM_FACTOR = .08f;

	private static final Long SEED = null;

	private static final String RULE = "B3/S23";

	private int universeSize = UNIVERSE_SIZE;

	private FBO fbo;

	private Texture2D renderTexture;

	private Texture2D computeTexture;

	private Program computeProgram;

	private boolean play;
	
	private TimeTracker timeTracker;

	@Override
	protected String getTitle() {
		return "Game of Life (OpenGL)";
	}

	@Override
	protected int initialViewWidth() {
		return Math.min(1024, universeSize);
	}

	@Override
	protected int initialViewHeight() {
		return initialViewWidth();
	}

	@Override
	protected boolean isDebugEnabled() {
		return DEBUG;
	}

	@Override
	protected boolean isVSyncEnabled() {
		return VSYNC;
	}

	@Override
	protected Program createRenderProgram() {
		Program program = Program.builder()
			.with(Shader.vertexShader("shaders/render.vs.glsl"))
			.with(Shader.fragmentShader("shaders/render.fs.glsl"))
			.build();

		return program;
	}

	@Override
	protected void loadResources() {
		super.loadResources();

		RuleConfiguration ruleConfiguration = new RuleConfiguration(RULE);
		Map<String, String> holderValue = Collections.singletonMap("rule",
				RuleToArray.createFloatArrays(ruleConfiguration));
		computeProgram = Program.builder()
			.with(Shader.vertexShader("shaders/render.vs.glsl"))
			.with(Shader.fragmentShader("shaders/compute.fs.glsl", holderValue))
			.build();

		renderTexture = createTexture(createRandomUniverse());
		computeTexture = createTexture(null);

		fbo = FBO.builder().build();
	}

	private Texture2D createTexture(ByteBuffer data) {
		return Texture2D.builder(universeSize, universeSize)
			.withInternalFormat(TextureInternalFormat.RGBA)
			.wrapR(TextureWrap.REPEAT)
			.wrapS(TextureWrap.REPEAT)
			.minFilter(TextureMinFilter.NEAREST)
			.magFilter(TextureMagFilter.NEAREST)
			.withData(TextureFormat.RGB, TextureDataType.UNSIGNED_BYTE, data)
			.build();
	}

	private void computeNext() {
		fbo.bind();
		fbo.attach(computeTexture);
		
		glViewport(0, 0, universeSize, universeSize);
		timeTracker.track(this::doCompute);

		fbo.unbind();

		Texture2D swap = renderTexture;
		renderTexture = computeTexture;
		computeTexture = swap;
	}

	private void doCompute() {
		computeProgram.use();
		glClear(GL_COLOR_BUFFER_BIT);
		draw();
		fbo.checkCompletion();
	}

	@Override
	protected void render() {
		glViewport(0, 0, viewWidth, viewHeight);
		renderTexture.bind();
		super.render();
		
		if (play) {
			computeNext();
		}
	}

	@Override
	protected void keyPressed(int key, int scancode, int mods) {
		switch (key) {
		case GLFW_KEY_SPACE:
			play = !play;
			if (play)
				timeTracker = new TimeTracker();
			break;
		case GLFW_KEY_ENTER:
			computeNext();
			break;
		case GLFW_KEY_R:
			renderTexture.updateData(TextureFormat.RGB, TextureDataType.UNSIGNED_BYTE, createRandomUniverse());
			break;
		default:
			super.keyReleased(key, scancode, mods);
		}
	}

	@Override
	protected void cleanup() {
		fbo.delete();
		renderTexture.delete();
		computeTexture.delete();
		computeProgram.delete();
		super.cleanup();
	}

	private ByteBuffer createRandomUniverse() {
		ByteBuffer buffer = createUniverseBuffer();
		long seed = SEED == null ? System.currentTimeMillis() : SEED;
		Random random = new Random(seed);
		visitUniverse((x, y) -> {
			float pct = random.nextFloat();
			byte color = (byte) (pct < RANDOM_FACTOR ? 255 : 0);
			buffer.put(color).put(color).put(color);
		});

		buffer.flip();
		return buffer;
	}

	private ByteBuffer createUniverseBuffer() {
		return BufferUtils.createByteBuffer(universeSize * universeSize * 3);
	}

	private void visitUniverse(UniverVisitor visitor) {
		for (int y = 0; y < universeSize; y++) {
			for (int x = 0; x < universeSize; x++) {
				visitor.visit(x, y);
			}
		}
	}

	private interface UniverVisitor {
		void visit(int x, int y);
	}

	public static void main(String[] args) {
		new GameOfLife().start();
	}

}
