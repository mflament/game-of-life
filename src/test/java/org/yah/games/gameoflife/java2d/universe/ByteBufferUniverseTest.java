package org.yah.games.gameoflife.java2d.universe;

import org.yah.games.gameoflife.opengl.universe.ByteBufferUniverse;

public class ByteBufferUniverseTest extends AbstractUniverseTest {

	@Override
	protected Universe createUniverse(int size) {
		return new ByteBufferUniverse(size, size);
	}

}
