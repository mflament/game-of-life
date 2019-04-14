package org.yah.games.gameoflife.universe.databuffer;

import org.yah.games.gameoflife.universe.AbstractUniverseTest;
import org.yah.games.gameoflife.universe.Universe;
import org.yah.games.gameoflife.universe.databuffer.DataBufferUniverse;

public class DataBufferUniverseTest extends AbstractUniverseTest {

	@Override
	protected Universe createUniverse(int size) {
		return new DataBufferUniverse(size, size);
	}

}
