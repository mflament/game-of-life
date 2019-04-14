package org.yah.games.gameoflife.universe.array;

import org.yah.games.gameoflife.universe.AbstractUniverseTest;
import org.yah.games.gameoflife.universe.Universe;

public class ArrayUniverseTest extends AbstractUniverseTest {

	@Override
	protected Universe createUniverse(int size) {
		return new ArrayUniverse(size, size);
	}

}
