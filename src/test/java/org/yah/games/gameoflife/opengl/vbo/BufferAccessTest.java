package org.yah.games.gameoflife.opengl.vbo;

import static org.junit.Assert.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_COPY;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.GL_STATIC_COPY;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_READ;
import static org.lwjgl.opengl.GL15.GL_STREAM_COPY;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_READ;

import org.junit.Test;
import org.yah.games.gameoflife.opengl.vbo.BufferAccess.Frequency;
import org.yah.games.gameoflife.opengl.vbo.BufferAccess.Nature;

public class BufferAccessTest {

	@Test
	public void testGlUsage() {
		assertEquals(GL_STATIC_DRAW, new BufferAccess(Frequency.STATIC, Nature.DRAW).glUsage());
		assertEquals(GL_STATIC_READ, new BufferAccess(Frequency.STATIC, Nature.READ).glUsage());
		assertEquals(GL_STATIC_COPY, new BufferAccess(Frequency.STATIC, Nature.COPY).glUsage());
		
		assertEquals(GL_DYNAMIC_DRAW, new BufferAccess(Frequency.DYNAMIC, Nature.DRAW).glUsage());
		assertEquals(GL_DYNAMIC_READ, new BufferAccess(Frequency.DYNAMIC, Nature.READ).glUsage());
		assertEquals(GL_DYNAMIC_COPY, new BufferAccess(Frequency.DYNAMIC, Nature.COPY).glUsage());
		
		assertEquals(GL_STREAM_DRAW, new BufferAccess(Frequency.STREAM, Nature.DRAW).glUsage());
		assertEquals(GL_STREAM_READ, new BufferAccess(Frequency.STREAM, Nature.READ).glUsage());
		assertEquals(GL_STREAM_COPY, new BufferAccess(Frequency.STREAM, Nature.COPY).glUsage());
	}

}
