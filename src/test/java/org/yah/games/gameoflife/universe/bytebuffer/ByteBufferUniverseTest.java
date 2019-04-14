package org.yah.games.gameoflife.universe.bytebuffer;

import org.yah.games.gameoflife.universe.AbstractUniverseTest;
import org.yah.games.gameoflife.universe.Universe;

public class ByteBufferUniverseTest extends AbstractUniverseTest {

	@Override
	protected Universe createUniverse(int size) {
		return new ByteBufferUniverse(size, size);
	}

}
